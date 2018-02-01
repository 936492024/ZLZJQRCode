package com.example.asus.zlzjqrcode.base;

/**
 * Created by asus on 2017/9/22.
 */

/**
 * 系统常量
 * @author fly
 */
public class SystemConstant {


    /**
     * SharedPreferences存储名
     */
    public static final String SHARED_PREFERENCE_NAME = "ZhuLaoZhiJia";
    /**
     * 运营服务器
     */
    public static final String HTTP = "http://tongji.zlzjcyw.com/";

    /**
     * 公共接口
     */
    public static final class PublicConstant{

        //首页公告
        public static final String Public_SECRET="qrcode";

        //身份证正则
        public static final String id_regular="^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

        //验证码接口
        public static final String API_SMS=HTTP
                + "api/sms";

        //扫描二维码接口
        public static final String API_QRCODE=HTTP
                + "api/qrcode";

        //生成二维码字符串
        public static final String API_STR=HTTP
                + "api/str";

        //登录接口
        public static final String API_LOGIN=HTTP
                + "api/login";

        //查询是否是首次登录
        public static final String API_IS_ONE=HTTP
                + "api/is-one";


        //初次登录(完善信息)
        public static final String API_ONE_LOGIN=HTTP
                + "api/one-login";

        //自动登录
        public static final String API_AUTO_LOGIN=HTTP
                + "api/auto-login";

        //添加合伙人
        public static final String API_ADD_PARTNER=HTTP
                + "api/add-partner";

        //查询下级负责人地区
        public static final String API_AREA=HTTP
                + "api/area";


        //查询区域内合伙人和用户
        public static final String API_COUNT=HTTP
                + "api/count";


        //负责区域/下级合伙人/和用户数量数据
        public static final String API_DATA=HTTP
                + "api/data";


        //app更新
        public static final String API_CHECK=HTTP
                + "api/check";

        //修改登录密码
        public static final String API_PASSWORD=HTTP
                + "api/password";

        //忘记密码
        public static final String API_FOR_GET_PASSWORD=HTTP
                + "api/for-get-password";

        //停用下级账号
        public static final String API_IS_SHOW=HTTP
                + "api/is-show";


        //修改下级合伙人手机号
        public static final String API_SET_MOBILE=HTTP
                + "api/set-mobile";


        //G级登录获取所有城市合作负责人
        public static final String API_GET_CITYS=HTTP
                + "api/get-citys";


        //根据member_id查询下级数据
        public static final String API_GET_AREAS=HTTP
                + "api/get-areas";



        //根据member_id查询下级数据
        public static final String API_GET_LEVEL=HTTP
                + "api/get-level";




        //根据member_id查询下级数据
        public static final String API_GET_TOTAL=HTTP
                + "api/get-total";


        //根据member_id查询下级数据
        public static final String API_GET=HTTP
                + "api/get-teamer";


    }

}

