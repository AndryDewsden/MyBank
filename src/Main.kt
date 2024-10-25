import java.time.LocalDate
import java.util.Scanner
import kotlin.math.roundToInt

data class Bank(
    val name: String,
    val shortName: String,
    val interestRate: Double
)

data class Individual(
    val fullName: String,
    val inn: String,
    val passportNumber: String,
    val passportSeries: String,
)

data class Account(
    val userAccount: MutableList<Individual> = mutableListOf(),
    var bankAccount: MutableList<BankAccount> = mutableListOf(),
    val currentAccounts: MutableList<CurrentAccount> = mutableListOf(),
    val creditAccounts: MutableList<CreditAccount> = mutableListOf(),
    val depositAccounts: MutableList<DepositAccount> = mutableListOf()
)

abstract class BankAccount(
    val bank: Bank,
    var balance: Double
)

class ActiveBank(activeBank: Bank)

class ActiveUser(
    val individual: Individual
)

class CurrentAccount(bank: Bank) : BankAccount(bank, 0.0) {
    fun deposit(amount: Double) {
        if (amount > 0) {
            balance += amount
            println("Внесено: $amount. \nБаланс текущего счета: $balance")
        } else {
            println("Сумма должна быть положительной.")
        }
    }

    fun withdraw(amount: Double) {
        if (amount <= balance) {
            balance -= amount
            println("Снято: $amount. \nБаланс текущего счета: $balance")
        } else {
            println("Недостаточно средств на счете.")
        }
    }
}

class CreditAccount(
    bank: Bank,
    val loanAmount: Double,
    val interestRate: Double,
    val termMonths: Int,
    val startDate: LocalDate
) : BankAccount(bank, loanAmount) {
    private var paidAmount: Double = 0.0

    fun monthlyPayment(): Double {
        val monthlyInterest = loanAmount * (interestRate / 100) / 12
        return (loanAmount / termMonths) + monthlyInterest }

    fun pay(amount: Double) {
        if (amount >= monthlyPayment()) {
            paidAmount += amount
            balance -= amount
            println("Оплачено: $amount. \nОстаток по кредиту: $balance")
        } else {
            println("Сумма платежа недостаточна.")
        }
    }
}

class DepositAccount(
    bank: Bank,
    var termMonths: Int,
    var minimumBalance: Double,
    var isWithdrawable: Boolean,
    var interestRate: Double
) : BankAccount(bank, 0.0) {
    fun addFunds(amount: Double) {
        if (amount >= minimumBalance) {
            balance += amount
            println("Внесено на депозит: $amount. \nБаланс депозитного счета: $balance")
        } else {
            println("Сумма должна быть не менее $minimumBalance.")
        }
    }

    fun calculateInterest(): Double {
        return balance * (interestRate / 100) / 12
    }

    fun addInterest() {
        val interest = calculateInterest()
        balance += interest
        println("Начислены проценты: $interest. \nБаланс депозитного счета: $balance")
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val individual = Individual("Иванов Иван Иванович", "1234567890", "1234", "567890")
    val bank = Bank("Банк 1", "Б1", 1.5)

    val bankList: MutableList<Bank> = mutableListOf()
    val userList: MutableList<Individual> = mutableListOf()
    val accountList: MutableList<Account> = mutableListOf()
    var activeBank: Bank? = null
    var activeUser: Individual? = null

    while (true) {
        println("\nВведите команду (help для списка команд):")
        val command = scanner.nextLine()

        when (command) {
            "help" -> {
                println("\nДоступные команды:")
                //создание
                println("")
                println("create_bank - Создать новый банк")
                println("create_user - Создать новое физическое лицо")
                //выбор
                println("")
                println("choose_bank - Выбрать банк")
                println("choose_user - Выбрать физическое лицо")
                //удаление
                println("")
                println("delete_bank - Удалить банк")
                println("delete_user - Удалить физическое лицо")
                //
                println("")
                println("create_current - Создать текущий счет")
                println("deposit - Внести деньги на текущий счет")
                println("withdraw - Снять деньги с текущего счета")
                //
                println("")
                println("create_deposit - Создать депозитный счет")
                println("add_funds - Внести деньги на депозитный счет")
                println("add_interest - Начислить проценты на депозитный счет")
                //
                println("")
                println("create_credit - Создать кредитный счет")
                println("pay_credit - Погасить кредит")
                //
                println("")
                println("list_bank - Список банков")
                println("list_user - Список физических лиц")
                //
                println("")
                println("exit - Выйти из приложения")
            }

            "create_bank" -> {
                println("Введите полное наименование")
                val bankNameFull = scanner.nextLine()

                println("Введите краткое наименование")
                val bankNameShort = scanner.nextLine()
                val makebank = Bank(bankNameFull, bankNameShort, 1.5)

                println("Новый банк создан. \nПолное наименование: ${makebank.name}\nКраткое наименование: ${makebank.shortName}")
                //bankList += makebank
                bankList.add(makebank)
            }

            "create_user" -> {
                println("Введите ФИО")
                val a = scanner.nextLine()

                println("Введите ИНН")
                val b = scanner.nextLine()

                println("Введите номер паспорта")
                val c = scanner.nextLine()

                println("Введите серию паспорта")
                val d = scanner.nextLine()
                val makeuser = Individual(a, b, c, d)

                println("Новое физическое лицо создано. \nФИО: ${makeuser.fullName}\nИНН: ${makeuser.inn}\nНомер паспорта: ${makeuser.passportNumber}\nСерия паспорта: ${makeuser.passportSeries}")
                //userList += makeuser
                userList.add(makeuser)
            }

            "choose_bank" -> {
                if (bankList.isNotEmpty()) {
                    println("Введите индекс банка (0-${bankList.size - 1}):")
                    val a = scanner.nextInt()
                    scanner.nextLine()
                    activeBank = bankList[a]
                    println("Выбран банк: ${activeBank?.name}")
                } else {
                    println("Банков пока что не существует. Создайте один с помощью команды create_bank")
                }
            }

            "choose_user" -> {
                if (userList.isNotEmpty()) {
                    println("Введите индекс пользователя (0-${userList.size - 1}):")
                    val a = scanner.nextInt()
                    scanner.nextLine()
                    activeUser  = userList[a]
                } else {
                    println("Физических лиц пока что не существует. Создайте один с помощью команды create_user")
                }
            }

            "delete_bank" -> {
                println("Введите индекс банка для удаления (0-${bankList.size - 1}):")
                val a = scanner.nextInt()
                scanner.nextLine()
                bankList.removeAt(a)
                println("Банк удален.")
            }

            "delete_user" -> {
                println("Введите индекс пользователя для удаления (0-${userList.size - 1}):")
                val a = scanner.nextInt()
                scanner.nextLine()
                userList.removeAt(a)
                println("Пользователь удален.")
            }

            "create_current" -> {
                if (activeBank != null) {
                    val currentAccount = CurrentAccount(activeBank!!) // Передаем активный банк
                    // Здесь вам нужно создать экземпляр Account, если его еще нет
                    val account = Account()
                    account.currentAccounts.add(currentAccount) // Добавляем текущий счет
                    println("Текущий счет создан.")
                } else {
                    println("Сначала выберите банк с помощью команды choose_bank.")
                }
            }

            "deposit" -> {
                //println("Введите сумму для внесения:")
                //val amount = scanner.nextDouble()
                //scanner.nextLine()
                //if (Account.currentAccounts.isNotEmpty()) {
                    //Account.currentAccounts[0].deposit(amount)
                //} else {
                    //println("У вас нет текущих счетов.")
                //}
            }

            "withdraw" -> {
                //println("Введите сумму для снятия:")
                //val amount = scanner.nextDouble()
                //scanner.nextLine()
                //if (individual.currentAccounts.isNotEmpty()) {
                    //individual.currentAccounts[0].withdraw(amount)
                //} else {
                    //println("У вас нет текущих счетов.")
                //}
            }

            "create_deposit" -> {
                //println("Введите минимальную сумму для депозита:")
                //val minimumBalance = scanner.nextDouble()
                //println("Введите срок депозита (в месяцах):")
                //val termMonths = scanner.nextInt()
                //println("Введите процентную ставку:")
                //val interestRate = scanner.nextDouble()
                //scanner.nextLine()
                //val depositAccount = DepositAccount(bank, termMonths, minimumBalance, false, interestRate)
                //individual.depositAccounts.add(depositAccount)
                //println("Депозитный счет создан.")
            }

            "add_funds" -> {
                //println("Введите сумму для внесения на депозит:")
                //val amount = scanner.nextDouble()
                //scanner.nextLine()
                //if (individual.depositAccounts.isNotEmpty()) {
                    //individual.depositAccounts[0].addFunds(amount)
                //} else {
                    //println("У вас нет депозитных счетов.")
                //}
            }

            "add_interest" -> {
                //if (individual.depositAccounts.isNotEmpty()) {
                    //individual.depositAccounts[0].addInterest()
                //} else {
                    //println("У вас нет депозитных счетов.")
                //}
            }

            "create_credit" -> {
                //println("Введите сумму кредита:")
                //val loanAmount = scanner.nextDouble()
                //println("Введите процентную ставку:")
                //val interestRate = scanner.nextDouble()
                //println("Введите срок кредита (в месяцах):")
                //val termMonths = scanner.nextInt()
                //scanner.nextLine()
                //val creditAccount = CreditAccount(bank, loanAmount, interestRate, termMonths, LocalDate.now())
                //individual.creditAccounts.add(creditAccount)
                //println("Кредитный счет создан.")
            }

            "pay_credit" -> {
                //println("Введите сумму для погашения кредита:")
                //val amount = scanner.nextDouble()
                //scanner.nextLine()
                //if (individual.creditAccounts.isNotEmpty()) {
                    //individual.creditAccounts[0].pay(amount)
                //} else {
                    //println("У вас нет кредитных счетов.")
                //}
            }

            "list_bank" -> {
                if(bankList.isNotEmpty()) {
                    println("\nСписок банков:")
                    println("№ | Полное наименование: | Краткое наименование: | Ставка:")
                    bankList.forEachIndexed { index, individual ->
                        println("${index}. | ${individual.name} | ${individual.shortName} | ${individual.interestRate}")
                    }
                } else {
                    println("Банков пока что не существует. Создайте один с помощью команда create_bank")
                }
            }

            "list_user" -> {
                if(userList.isNotEmpty()) {
                    println("\nСписок физических лиц:")
                    println("№ | ФИО: | ИНН: | Номер паспорта: | Серия паспорта:")
                    userList.forEachIndexed { index, individual ->
                        println("${index}. | ${individual.fullName} | ${individual.inn} | ${individual.passportNumber} | ${individual.passportSeries}")
                    }
                } else {
                    println("Физических лиц пока что не существует. Создайте один с помощью команда create_user")
                }
            }

            "list_current" -> {
                if(accountList.isNotEmpty()) {
                    println()
                    println()

                }
                else {
                    println()
                }
            }

            "exit" -> {
                println("\nВыход из приложения.")
                //break
                return
            }

            else -> {
                println("\nНеизвестная команда. Введите 'help' для получения списка команд.")
            }
        }
    }
}