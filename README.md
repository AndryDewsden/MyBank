"Мой банк"
приложение, реализующее следующий функционал:

1. Пользователь может создать несколько банков.
2. Пользователь может создать несколько физлиц.
3. Банк обладает следующими свойствами: наименование банка (полное и краткое).
4. Физлицо имеет следующие свойства: ФИО, ИНН, номер паспорта, серия паспорта.
5. Любой банк имеет следующие виды банковских счетов:
- текущий счет (используется для пополнения и снятия с него денежных средств физлицами)
- кредитный счет (используется физлицами для выплаты обязательств по кредиту)
- депозитный счет (используется физлицами для хранения вкладов и получения дохода)
6. Каждый банк устанавливает свой процент на остаток средств по текущему счету (от 0.1% до 2% годовых). Процент одинаков для любого клиента банка. Проценты начисляются ежемесячно, первого числа, на минимальный остаток в прошедшем месяце или периоде, если счет открывался не первого числа, и зачисляются на текущий счет.
7. В случае закрытия текущего счета до истечения месяца проценты начисляются на минимальный остаток в прошедшем периоде.
8. Любое физлицо может открыть до трех текущих счетов в каждом из имеющихся банков.
9. Физлицо может переводить денежные средства между своими текущими счетами, как в пределах одного банка, так и между банками.
10. Любой клиент банка может внести денежные средства в любой сумме на текущий счет и снять их с текущего счета в пределах имеющегося остатка.
11. Физлицо может открыть не более одного депозитного счета в каждом из имеющихся банков. При открытии указывается срок депозита (не менее 3-х месяцев), минимальный остаток, пополняемый или не пополняемый, можно с него снимать денежные средства или нет, годовая процентная ставка (от 18% до 25%, может быть различна для разных пользователей даже в пределах одного банка), проценты начисляются ежедневно и с учетом капитализации (в конце текущих суток) или ежемесячно (в последний день месяца), пролонгируемый или не пролонгируемый.
12. Депозит с ежедневным начислением процентов всегда является пополняемым.
13. Зачисление на депозитный счет может производиться только с текущего счета физлица.
14. Ежедневная капитализация обозначает, что начисленные за день проценты участвуют при расчете суммы процентов за следующий день.
15. При ежедневном начислении проценты зачисляются первого числа следующего месяца за предыдущий месяц или период от даты открытия депозита, если депозит открывался не первого числа на депозитный счет.
16. При ежемесячном начислении сумма процентов вычисляется первого числа следующего месяца на минимальный остаток в прошедшем месяце и зачисляется на депозитный счет в случае, если депозит пополняемый. В случае не пополняемого депозита сумма процентов зачисляется на депозитный счет на следующий день после окончания срока депозита.
17. Если депозит открылся не первого числа прошедшего месяца, то сумма процентов вычисляется исходя из периода и условий депозита. В случае ежемесячного начисления процентов их сумма вычисляется пропорционально периода от даты открытия депозита до первого числа следующего месяца на минимальный остаток в прошедшем периоде.
18. Если срок окончания депозита не последнее число месяца, то сумма процентов вычисляется пропорционально периода от первого числа месяца закрытия до числа закрытия в случае ежемесячного начисления процентов.
19. Физлицо может переводить деньги на депозитные счета со своих текущих счетов, если это не противоречит условиям депозита.
20. Если с депозита, имеющего запрет на снятие денежных средств, снимаются деньги, то процент пересчитывается исходя из процентной ставки годовых для текущего счета в данном банке. Денежные средства в данном случае должны сняться полностью.
21. Если на депозите, имеющем разрешение на снятие, после снятия денежных средств остается меньше минимального остатка, то депозит закрывается и физлицу выдается вся имеющаяся сумма с учетом процентов.
22. В случае окончания срока пролонгируемого депозита данный депозит автоматически продлевается на условиях, при которых он открывался.
23. В случае окончания срока не пролонгируемого депозита, если у физлица имеется текущий счет в данном банке, то денежные средства с депозитного счета автоматически переводятся на любой текущий счет (если их несколько).
24. В случае окончания срока не пролонгируемого депозита, если у физлица не имеется текущий счет в данном банке, то депозитный счет автоматически становится текущим с условиями данного банка.
25. Любое физлицо может взять не более одного кредита в любом из имеющихся банков. При получении кредита указывается сумма кредита, процент кредита, срок (количество полных месяцев) пользования кредитом, дата взятия кредита.
26. Сумма платежа по кредиту рассчитывается как сумма кредита, деленная на количество месяцев плюс сумма процентов на остаток до вычета из тела кредита.
27. Кредиты можно гасить досрочно частично или полностью.
28. Кредит гасится путем внесения денежных средств с любого текущего счета физлица.
29. При частичном погашении сперва гасятся набежавшие на день платежа проценты, остаток от внесенной суммы вычитается из основного долга. Остаток кредита делится на количество оставшихся месяцев.
30. Частичное погашение кредита не уменьшает сумму платежа по кредиту в оговоренный день (дата взятия кредита), если досрочное погашение произошло до даты очередного платежа.

Пользователь должен иметь возможность получать следующие отчеты:
1. Наличие текущих счетов и остатков на них у физлица
2. Наличие кредитных счетов и остатков по ним у физлица
3. Наличие депозитных счетов и остатков на них у физлица
4. При открытии депозитного счета физлицо получает информацию о предполагаемом доходе

При открытии кредитного счета (взятии кредита) физлицо получает информацию о возможной переплате (сумме процентов) и график погашения кредита.

При частичном досрочном погашении кредита физлицо получает информацию об остатке кредита и новый график погашения кредита.

Перед полным погашением кредита физлицо получает информацию о сумме платежа, где учитывается остаток основного долга и проценты за пользование кредитом, рассчитанные пропорционально срока с момента последнего платежа.
