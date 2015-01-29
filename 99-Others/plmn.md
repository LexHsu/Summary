PLMN
===

### RPLMN

Registered PLMN，终端在上次关机或脱网前登记上的 PLMN，会临时保存在 USIM 卡上。

### HPLMN

Home PLMN，用户 USIM 对应 IMSI 的 PLMN。

### EHPLMN
EquivalentHome PLMN，HPLMN 对应的运营商可能会有不同的号段，
如中国移动有 46000、46002、46007 三个号段，
46002 相对 46000 就是 EHPLMN，运营商烧卡时写入USIM卡中。

### EPLMN

Equivalent PLMN，这个 PLMN 在 MSC 或者 MME 上配置（ADD PEERPLMN）。
既与当前网络 HPLMN 对等的 PLMN；如果用户在归属地那么 EPLMN = EHPLMN。如果在漫游地，EPLMN != EHPLMN。

### UPLMN
User Controlled PLMN 用户控制 PLMN。UPLMN 就是终端在用户手工选网时选择的 PLMN，USIM 卡会存储下来。

### OPLMN
Operator Controlled PLMN，运营商控制 PLMN。
合理的解释运营商烧卡时将与该运营商签署了漫游协议的运营商 PLMN 作为 OPLMN 写入 USIM 卡，作为后面用户选网的建议。

### FPLMN

Forbidden PLMN，为被禁止访问的PLMN，通常终端在尝试接入某个 PLMN 被拒绝以后，会将其加到本列表中。

### PLMN 选择顺序

1. RPLMN
2. EPLMN
3. HPLMN
4. EHPLMN
5. UPLMN
6. OPLMN
7. 其他PLMN

用户在接入网络时不仅要考虑接入本运营商网络还要考虑接入的时间。RPLMN作为上一次注册过的网络，从接入成功率以及接入时间来说肯定是最优的，EPLMN是上次注册网络的对等网络等同于RPLMN。其他PLMN是当前终端能搜索到的所有PLMN，当前面各种PLMN都匹配不上的话，终端将按信号强弱进行尝试接入。

由于综合各种原因接入的网络不一定是用户的归属运营商网络，因此用户接入网络之后还会发起小区重选流程，这时如果有HPLMN,EHPLMN的信号覆盖的话，会选择回到归属运营商网络。
