import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.Scanner

class Bank(val fullName: String, val shortName: String, val currentAccountInterestRate: Double) {
    init {
        require(currentAccountInterestRate in 0.1..2.0) { "Ставка должна быть от 0.1% до 2%." }
    }
}

open class BankAccount(val bank: Bank, var balance: Double) {
    fun deposit(amount: Double) {
        balance += amount
        println("Счет пополнен на сумму: $amount. Текущий баланс: $balance.")
    }

    fun withdraw(amount: Double) {
        if (amount <= balance) {
            balance -= amount
            println("Снято: $amount. Текущий баланс: $balance.")
        } else {
            println("Недостаточно средств для снятия.")
        }
    }
}

class CurrentAccount(bank: Bank) : BankAccount(bank, 0.0) {
    var minimumBalance: Double = 0.0

    fun checkAndApplyInterest() {
        val interest = (minimumBalance * (bank.currentAccountInterestRate / 100) / 12)
        balance += interest
        println("Начислены проценты: $interest. Текущий баланс: $balance")
    }

    fun closeAccount() {
        checkAndApplyInterest()
        println("Текущий счет закрыт. Остаток: $balance.")
        balance = 0.0
    }
}

class DepositAccount(
    bank: Bank,
    var depositTerm: Int,
    var minimumBalance: Double,
    var isReplenishable: Boolean,
    var isWithdrawable: Boolean,
    var annualInterestRate: Double,
    var isRenewable: Boolean,
    private var dailyInterest: Boolean
) : BankAccount(bank, minimumBalance) {

    private var currentBalance: Double = minimumBalance
    private var startDate: LocalDate = LocalDate.now()
    private var endDate: LocalDate = startDate.plusMonths(depositTerm.toLong())
    private var lastInterestDate: LocalDate = startDate

    init {
        if (depositTerm < 3) {
            throw IllegalArgumentException("Срок депозита должен быть не менее 3-х месяцев.")
        }
        if (annualInterestRate < 18.0 || annualInterestRate > 25.0) {
            throw IllegalArgumentException("Годовая процентная ставка должна быть от 18% до 25%.")
        }
    }

    fun depositFromCurrentAccount(amount: Double, currentAccount: CurrentAccount) {
        if (isReplenishable) {
            currentAccount.withdraw(amount)
            currentBalance += amount
            println("Пополнен депозит на сумму: $amount. Текущий баланс: $currentBalance")
        } else {
            println("Депозит не пополняемый.")
        }
    }

    fun withdraw(amount: Double) {
        if (isWithdrawable && amount <= currentBalance) {
            currentBalance -= amount
            println("Снято: $amount. Текущий баланс: $currentBalance")
        } else {
            println("Снятие средств не разрешено или недостаточно средств.")
        }
    }

    fun applyInterest() {
        val today = LocalDate.now()
        if (dailyInterest) {
            val daysSinceLastInterest = ChronoUnit.DAYS.between(lastInterestDate, today).toInt()
            if (daysSinceLastInterest > 0) {
                val interest = (currentBalance * (annualInterestRate / 100) / 365) * daysSinceLastInterest
                currentBalance += interest
                println("Начислены проценты за $daysSinceLastInterest дней: $interest. Текущий баланс: $currentBalance")
                lastInterestDate = today
            }
        } else {
            if (today.dayOfMonth == today.lengthOfMonth()) {
                val monthlyInterest = (currentBalance * (annualInterestRate / 100) / 12)
                currentBalance += monthlyInterest
                println("Начислены проценты за месяц: $monthlyInterest. Текущий баланс: $currentBalance")
            }
        }
    }

    fun checkEndDate(individual: Individual) {
        if (LocalDate.now() >= endDate) {
            println("Депозит завершен.")
            if (isRenewable) {
                println("Депозит автоматически продлевается.")
                startDate = LocalDate.now()
                endDate = startDate.plusMonths(depositTerm.toLong())
            } else {
                if (individual.currentAccounts.isNotEmpty()) {
                    val targetAccount = individual.currentAccounts[0]
                    targetAccount.deposit(currentBalance)
                    println("Средства переведены на текущий счет: ${targetAccount.balance}.")
                }
                currentBalance = 0.0
            }
        }
    }

    fun calculateExpectedIncome(): Double {
        return (currentBalance * (annualInterestRate / 100) * depositTerm) / 12
    }
}

class Loan(
    val amount: Double,
    val interestRate: Double,
    val termMonths: Int,
    val startDate: LocalDate
) {
    val endDate: LocalDate = startDate.plusMonths(termMonths.toLong())
    var isPaid: Boolean = false
    private var remainingBalance: Double = amount
    private var monthlyPayment: Double = calculateMonthlyPayment()

    private fun calculateMonthlyPayment(): Double {
        return (amount / termMonths) + (amount * (interestRate / 100) / 12)
    }

    fun makePayment(paymentAmount: Double) {
        if (isPaid) {
            println("Кредит уже погашен.")
            return
        }
        val interestPayment = (remainingBalance * (interestRate / 100) / 12)
        if (paymentAmount < interestPayment) {
            println("Недостаточная сумма для погашения процентов.")
            return
        }
        val principalPayment = paymentAmount - interestPayment
        remainingBalance -= principalPayment
        if (remainingBalance <= 0) {
            isPaid = true
            println("Кредит погашен полностью.")
        } else {
            println("Частичное погашение. Остаток долга: $remainingBalance.")
        }
    }

    fun getMonthlyPayment(): Double {
        return if (!isPaid) monthlyPayment else 0.0
    }

    fun calculateTotalInterest(): Double {
        return (amount * (interestRate / 100) * termMonths) / 12
    }

    fun getPaymentSchedule(): List<Pair<LocalDate, Double>> {
        val schedule = mutableListOf<Pair<LocalDate, Double>>()
        var remaining = amount
        for (i in 1..termMonths) {
            val interestPayment = (remaining * (interestRate / 100) / 12)
            val principalPayment = monthlyPayment - interestPayment
            remaining -= principalPayment
            schedule.add(Pair(startDate.plusMonths(i.toLong()), monthlyPayment))
        }
        return schedule
    }
}

class Individual(
    val fullName: String,
    val inn: String,
    val passportNumber: String,
    val passportSeries: String,
) {
    val currentAccounts: MutableList<CurrentAccount> = mutableListOf()
    var depositAccount: DepositAccount? = null
    var loan: Loan? = null

    fun addCurrentAccount(account: CurrentAccount) {
        if (currentAccounts.size < 3) {
            currentAccounts.add(account)
            println("Текущий счет добавлен.")
        } else {
            println("Вы можете открыть только 3 текущих счета.")
        }
    }

    fun addDepositAccount(account: DepositAccount) {
        if (depositAccount == null) {
            depositAccount = account
            println("Депозитный счет добавлен. Ожидаемый доход: ${account.calculateExpectedIncome()}.")
        } else {
            println("Вы можете открыть только один депозитный счет в этом банке.")
        }
    }

    fun takeLoan(amount: Double, interestRate: Double, termMonths: Int) {
        if (loan == null) {
            loan = Loan(amount, interestRate, termMonths, LocalDate.now())
            println("Кредит на сумму $amount оформлен. Возможная переплата: ${loan.calculateTotalInterest()}.")
            println("График погашения: ${loan.getPaymentSchedule()}.")
        } else {
            println("У вас уже есть активный кредит.")
        }
    }

    fun payLoan(paymentAmount: Double) {
        loan?.makePayment(paymentAmount) ?: println("У вас нет активного кредита.")
    }

    fun transferFunds(fromAccount: CurrentAccount, toAccount: CurrentAccount, amount: Double) {
        if (fromAccount.balance >= amount) {
            fromAccount.withdraw(amount)
            toAccount.deposit(amount)
            println("Переведено $amount с счета ${fromAccount.bank.shortName} на счет ${toAccount.bank.shortName}.")
        } else {
            println("Недостаточно средств для перевода.")
        }
    }

    fun reportCurrentAccounts() {
        println("Ваши текущие счета:")
        currentAccounts.forEachIndexed { index, account ->
            account.checkAndApplyInterest()
            println("${index + 1}: Баланс: ${account.balance}, Минимальный остаток: ${account.minimumBalance}")
        }
    }

    fun reportLoan() {
        loan?.let {
            println("Кредит: Остаток: ${it.remainingBalance}, Статус: ${if (it.isPaid) "Погашен" else "Активен"}")
        } ?: println("У вас нет активного кредита.")
    }

    fun reportDeposit() {
        depositAccount?.let {
            println("Депозит: Баланс: ${it.currentBalance}, Остаток: ${it.minimumBalance}, Статус: ${if (LocalDate.now() >= it.endDate) "Завершен" else "Активен"}")
        } ?: println("У вас нет депозитного счета.")
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val bankList: MutableList<Bank> = mutableListOf()
    val userList: MutableList<Individual> = mutableListOf()
    var active:User  Individual? = null
    var activeBank: Bank? = null

    while (true) {
        println("\nВведите команду (help для списка команд):")
        val command = scanner.nextLine()

        when (command) {
            "create_bank" -> {
                println("Введите полное наименование банка:")
                val fullName = scanner.nextLine()
                println("Введите краткое наименование банка:")
                val shortName = scanner.nextLine()
                println("Введите процентную ставку (от 0.1 до 2.0):")
                val interestRate = scanner.nextDouble()
                scanner.nextLine()
                val bank = Bank(fullName, shortName, interestRate)
                bankList.add(bank)
                println("Банк '$fullName' успешно создан.")
            }

            "create_user" -> {
                println("Введите ФИО:")
                val fullName = scanner.nextLine()
                println("Введите ИНН:")
                val inn = scanner.nextLine()
                println("Введите номер паспорта:")
                val passportNumber = scanner.nextLine()
                println("Введите серию паспорта:")
                val passportSeries = scanner.nextLine()
                val user = Individual(fullName, inn, passportNumber, passportSeries)
                userList.add(user)
                println("Пользователь '$fullName' успешно создан.")
            }

            "select_user" -> {
                println("Выберите пользователя (введите индекс):")
                userList.forEachIndexed { index, user -> println("$index: ${user.fullName}") }
                val userIndex = scanner.nextInt()
                scanner.nextLine()
                activeUser  = userList.getOrNull(userIndex)
                if (activeUser  != null) {
                    println("Выбран пользователь: ${activeUser .fullName}")
                } else {
                    println("Пользователь не найден.")
                }
            }

            "select_bank" -> {
                println("Выберите банк (введите индекс):")
                bankList.forEachIndexed { index, bank -> println("$index: ${bank.fullName}") }
                val bankIndex = scanner.nextInt()
                scanner.nextLine()
                activeBank = bankList.getOrNull(bankIndex)
                if (activeBank != null) {
                    println("Выбран банк: ${activeBank.fullName}")
                } else {
                    println("Банк не найден.")
                }
            }

            "create_current" -> {
                if (activeUser  != null && activeBank != null) {
                    val currentAccount = CurrentAccount(activeBank!!)
                    activeUser .addCurrentAccount(currentAccount)
                } else {
                    println("Сначала выберите банк и пользователя.")
                }
            }

            "create_deposit" -> {
                if (activeUser  != null && activeBank != null) {
                    println("Введите срок депозита (в месяцах):")
                    val depositTerm = scanner.nextInt()
                    println("Введите минимальный остаток:")
                    val minimumBalance = scanner.nextDouble()
                    println("Пополняемый? (true/false):")
                    val isReplenishable = scanner.nextBoolean()
                    println("Снимаемые средства? (true/false):")
                    val isWithdrawable = scanner.nextBoolean()
                    println("Введите годовую процентную ставку (от 18 до 25):")
                    val annualInterestRate = scanner.nextDouble()
                    println("Пролонгируемый? (true/false):")
                    val isRenewable = scanner.nextBoolean()
                    println("Ежедневное начисление процентов? (true/false):")
                    val dailyInterest = scanner.nextBoolean()
                    scanner.nextLine()

                    try {
                        val depositAccount = DepositAccount(activeBank!!, depositTerm, minimumBalance, isReplenishable, isWithdrawable, annualInterestRate, isRenewable, dailyInterest)
                        activeUser .addDepositAccount(depositAccount)
                    } catch (e: IllegalArgumentException) {
                        println(e.message)
                    }
                } else {
                    println("Сначала выберите банк и пользователя.")
                }
            }

            "list_current" -> {
                if (activeUser  != null) {
                    activeUser .reportCurrentAccounts()
                } else {
                    println("Сначала выберите пользователя.")
                }
            }

            "report_loan" -> {
                if (activeUser  != null) {
                    activeUser .reportLoan()
                } else {
                    println("Сначала выберите пользователя.")
                }
            }

            "report_deposit" -> {
                if (activeUser  != null) {
                    activeUser .reportDeposit()
                } else {
                    println("Сначала выберите пользователя.")
                }
            }

            "take_loan" -> {
                if (activeUser  != null) {
                    println("Введите сумму кредита:")
                    val amount = scanner.nextDouble()
                    println("Введите процент кредита:")
                    val interestRate = scanner.nextDouble()
                    println("Введите срок кредита (в месяцах):")
                    val termMonths = scanner.nextInt()
                    scanner.nextLine()
                    activeUser .takeLoan(amount, interestRate, termMonths)
                } else {
                    println("Сначала выберите пользователя.")
                }
            }

            "pay_loan" -> {
                if (activeUser  != null) {
                    println("Введите сумму платежа:")
                    val paymentAmount = scanner.nextDouble()
                    scanner.nextLine()
                    activeUser .payLoan(paymentAmount)
                } else {
                    println("Сначала выберите пользователя.")
                }
            }

            "transfer_funds" -> {
                if (activeUser  != null) {
                    println("Выберите счет для перевода (введите индекс):")
                    activeUser .currentAccounts.forEachIndexed { index, account -> println("$index: ${account.bank.shortName}, Баланс: ${account.balance}") }
                    val fromIndex = scanner.nextInt()
                    val fromAccount = activeUser .currentAccounts.getOrNull(fromIndex)

                    println("Выберите счет для получения (введите индекс):")
                    activeUser .currentAccounts.forEachIndexed { index, account -> println("$index: ${account.bank.shortName}, Баланс: ${account.balance}") }
                    val toIndex = scanner.nextInt()
                    val toAccount = activeUser .currentAccounts.getOrNull(toIndex)

                    println("Введите сумму для перевода:")
                    val amount = scanner.nextDouble()
                    scanner.nextLine()

                    if (fromAccount != null && toAccount != null) {
                        activeUser .transferFunds(fromAccount, toAccount, amount)
                    } else {
                        println("Некорректные счета.")
                    }
                } else {
                    println("Сначала выберите пользователя.")
                }
            }

            "check_deposit" -> {
                if (activeUser  != null && activeUser .depositAccount != null) {
                    activeUser .depositAccount!!.checkEndDate(activeUser )
                } else {
                    println("У вас нет депозитного счета.")
                }
            }

            "exit" -> {
                println("Выход из приложения.")
                return
            }

            else -> {
                println("Неизвестная команда.")
            }
        }
    }
}
