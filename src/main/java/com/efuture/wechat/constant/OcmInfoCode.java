package com.efuture.wechat.constant;

public interface OcmInfoCode {

	final String OCM_Center = "001"; // 卡中心代码
	final String NOT_DEFINE = ResponseCode.PROXY_NOT_DEFINE;

	// 卡片状态
	interface CardFlag {
		final String BlankCard = "01"; // 待制卡
		final String Writed = "02"; // 待领用
		final String WaitForIssue = "03"; // 待发售
		final String Normal = "04"; // 正常使用
		final String ManualFrozen = "05"; // 冻结
		final String AutoFrozen = "06"; // 自动冻结
		final String Rejected = "07"; // 注销
		final String Recycled = "08"; // 已回收
		final String WaitForActive = "09"; // 待激活
		final String KeepByWorker = "P1"; // 个人领用状态
	}
	
	// TransCode
	interface TransCode {
		final String AUTH = "A"; //认证
		final String NewCust = "11"; // 新增会员
		final String NewIden = "12"; // 新增认证
		final String UpdateIden = "13"; // 更新认证
		final String BindIden = "14"; // 绑定认证
		final String DisBindCust = "15"; // 解绑会员
		final String DisBindIden = "16"; // 解绑认证
		final String Reject = "17"; // 注销
		final String UpdateInfo = "18"; //更新资料
	}

	// 卡用途
	interface CardScope {
		final String All = "a"; 			// 全渠道使用
		final String RegStore = "m"; 		// 线下注册店
		final String RealStore = "n"; 		// 线下所有门店
	}

	// 卡用途
	interface CardPurpose {
		final String Normal = "1"; // 普通会员卡
		final String TempCard = "2"; // 临时卡
		final String CouponCard = "3"; // 返券卡
		final String Employee = "4"; // 员工卡
		final String GroupSale = "5"; // 团购卡
		final String BGJGK = "6"; // 军供卡
	}

	// 认证方式
	interface IdMethod {
		final String Card = "1"; // 卡号
		final String Mobile = "2"; // 手机号
		final String EMail = "3"; // 邮箱
		final String QQ = "4"; // QQ
		final String WeChat = "5"; // 微信
		final String Alipay = "6"; // 支付宝
		final String Weibo = "7"; // 微博
		final String Track = "A"; // 磁道
		final String QrCode = "B"; // 二维码
		final String Cid = "C"; // CID
		final String BarCode = "D"; // 条码
		final String PlateNo = "8";// 车牌号
		final String WxUnionID = "9"; // 微信UnionID
		final String FaceImage = "10"; //人脸图片
		final String Koubei = "11"; //口碑
	}

	// NSTA
	interface Nsta {
		final int Normal = 2021; // 正常
		final int Invalid = 2022; // 伪删除
		final int Deleted = 2023; // 伪删除
	}
		
	// AuthFailure 20xxx
	// 10000 // Standard Error：JSON错误，传参错误
	interface AuthFailure
	{
		final String NotExists = "20000";		// 信息不存在或者未绑定
		final String ParseFailure = "20001";	// 解析失败（磁道、二维码、条码）
		final String Frozen = "20002";			// 冻结
		final String Rejected = "20003";		// 注销
		final String PassFailure = "20004";		// 密码错误
		final String DictError = "20005";		// 字典错误：类别错误、用途错误
		final String PrivFailure = "20006";		// 权限错误：无回馈权限
		final String ScopeFailure = "20007";	// 使用范围错误：非本门店使用
		final String UnSupport = "20099";		// 不支持
	}

	interface QRFailure
	{
		final String ParaError = "21000";		// 传入参数错误
		final String ParseFailure = "21001";	// 格式错误
		final String ChecksumFail = "21002";	// 校验失败
		final String ExpireFail = "21003";		// 过期失效
	}
	
	// RegFailure 22xxx
	// 10000 // Standard Error：JSON错误，传参错误
	interface RegFailure
	{
		final String DupKeyWord = "22001";		// 重复的注册关键字
		final String DupIdNo = "22002";			// 重复的证件号
		final String MissGrade = "22003";		// 未配置默认等级
	}
	
	interface BindFailure
	{
		final String NotExists = "23000";		// 信息不存在或者未绑定
		final String DupIdNo = "22001";			// 证件号重复
		final String MissGrade = "22002";		// 等级错误
	}

	interface ImageType
	{
		final String FaceImage = "1"; //人脸图片
	}

	interface EntMsgType
	{
		final String RegMsg = "001"; //注册成功通知
		final String ConsumeMsg = "002"; //消费提醒通知
		final String CouponDueMsg = "003"; //电子券回收提醒
		final String GetCouponMsg = "004"; //发券通知
		final String PerfectInfo = "005";	//会员资料完善
		final String PointChangeMsg = "006";	//积分变更通知
		final String CouponUseMsg = "007";	//券核销通知
		final String CouponUseCancelMsg = "008";	//券核销通知
		final String MultiDepositMsg = "009";	//券核销通知
		final String JoinEventMsg = "010"; //活动报名成功通知
		final String JoinExpireMsg = "011"; //活动门票到期通知
		final String AseembleMsg = "012"; //拼团成功通知
		final String AseembleCancelMsg = "013"; //拼团解散通知
		final String CustTypeChangeMsg = "014"; //会员等级变动提醒
		final String CarEntryMsg = "015"; //车辆入场消息提醒
	}

	interface WxCardFailure
	{
		final String NEWCARD = "24000";       //申请失败
		final String WX_CREATE = "24001";    //微信审核失败
	}

	/**
	 * 微信卡券背景颜色
	 */
	interface WxCardColor
	{
		final String Color010 = "Color010"; //63b359
		final String Color020 = "Color020"; //2c9f67
		final String Color030 = "Color030"; //509fc9
		final String Color040 = "Color040"; //5885cf
		final String Color050 = "Color050"; //9062c0
		final String Color060 = "Color060"; //d09a45
		final String Color070 = "Color070"; //e4b138
		final String Color080 = "Color080"; //ee903c
		final String Color081 = "Color081"; //f08500
		final String Color082 = "Color082"; //a9d92d
		final String Color090 = "Color090"; //dd6549
		final String Color100 = "Color100"; //cc463d
		final String Color101 = "Color101"; //cf3e36
		final String Color102 = "Color102"; //5E6671
	}

	/**
	 * 微信卡券有效期
	 */
	interface WxCardDateType
	{
		final String FixTimeRange = "DATE_TYPE_FIX_TIME_RANGE"; //固定日期区间
		final String FixTerm = "DATE_TYPE_FIX_TERM";  //固定时长
		final String Permanent = "DATE_TYPE_PERMANENT"; //永久有效
	}

	/**
	 * 会员Code展示类型
	 */
	interface WxCardCodeType
	{
		final String Text = "CODE_TYPE_TEXT"; //文本
		final String Barcode = "CODE_TYPE_BARCODE";  //条码
		final String QRcode = "CODE_TYPE_QRCODE";  //二维码和文本
		final String OnlyQRcode = "CODE_TYPE_ONLY_QRCODE"; //只有二维码
	}

	/**
	 * 微信会员激活支持字段
	 */
	interface WxCardCommonField
	{
		final String Mobile = "USER_FORM_INFO_FLAG_MOBILE"; //手机号
		final String Sex = "USER_FORM_INFO_FLAG_SEX";      //性别
		final String Name = "USER_FORM_INFO_FLAG_NAME";    //姓名
		final String Birthday = "USER_FORM_INFO_FLAG_BIRTHDAY";  //出生日期
		final String IDcard = "USER_FORM_INFO_FLAG_IDCARD";   //身份证
		final String EMail = "USER_FORM_INFO_FLAG_EMAIL";     //邮箱
		final String Location = "USER_FORM_INFO_FLAG_LOCATION";  //详细地址
		final String Education = "USER_FORM_INFO_FLAG_EDUCATION_BACKGRO";  //教育
		final String Industry = "USER_FORM_INFO_FLAG_INDUSTRY";  //行业
		final String Income = "USER_FORM_INFO_FLAG_INCOME";     //收入
		final String Habit = "USER_FORM_INFO_FLAG_HABIT";       //爱好
	}

	/**
	 * 微信卡券类型
	 */
	interface WxCardType
	{
		final String MemberCard = "MEMBER_CARD"; //会员卡
		final String Groupon = "GROUPON";  //团购券
		final String Discount = "DISCOUNT";  //折扣券
		final String Gift = "GIFT";  //礼品券
		final String Cash = "CASH";  //代金券
		final String GeneralCoupon = "GENERAL_COUPON"; //通用券
	}

	interface GameFailure
	{
		final String NotExists = "25000";		// 游戏不存在
		final String NotQualified = "25001";    //没有参与资格
	}

	interface AwardType
	{
		final String JF = "JF";
		final String Coupon = "COUPON";
		final String No = "No";
	}

	interface CustType
	{
		final String HALL = "HALL";
	}

	interface GameJoinType
	{
		final String Free = "01";
		final String Point = "02";
		final String FreeAndPoint = "03";
		final String Sale = "04"; //消费抽奖
	}

	//会员账户类型
	interface CustAccntGroup
	{
		final String Point = "01"; //积分账户
		final String Coupon = "02"; //券账户
		final String Balance = "03"; //余额账户
		final String CZ = "04"; //成长值账户
		final String Rule = "05"; //规则券
		final String XF = "06"; //消费
		final String DX = "07"; //定向积分
	}

	//微信字段类型
    public interface ParamType {
		final String Custom = "1";  //自定义类型
		final String Variable = "0";  //业务变量
	}

	//签到类型
	public interface SigninType {
		final String Signin = "1";  //已签
		final String AddSignin = "2";  //补签
	}

	//签到周期类型
    public interface SignPeriodType {
		String Week = "0";    //自然周
		String Month = "1";   //自然月
		String Quarter = "2"; //季度
		String Year = "3";    //自然年
		String Custom = "4";  //自定义
	}

	//发送类型 1-微信消息 2-短信消息
    public interface SendType {
		String Wechat = "1";
		String Sms = "2";
    }

    //执行类型 1-立即发送 2-定时发送
	public interface ExecuteType {
		String Now = "1";
		String Delay = "2";
	}

	//发送会员类型 1-所有会员 2-自定义会员
	public interface SendCustType {
		String ALL = "1";
		String Custom = "2";
	}
}
