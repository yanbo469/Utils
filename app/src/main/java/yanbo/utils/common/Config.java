package yanbo.utils.common;

import android.os.Environment;

/**
 * 可变的参数
 *
 * @author Cuizhen
 */
public interface Config {

    /**
     * 请求链接
     */
    String BASE_URL = "http://api.server-x.net/v1/";

    /**
     * 用户协议
     */
    String USER_AGREEMENT_URL = "http://api.xiongmaotaoxue.com/v1/public/rulesinfo";

    /**
     * 网络请求延时
     */
    int HTTP_TIMEOUT = 5000;

    /**
     * 下拉刷新和上拉加载的超时时长
     */
    int REFRESH_AND_LOAD_MORE_DELAY = HTTP_TIMEOUT;

    /**
     * 下拉刷新和上拉加载是否启用越界回弹
     */
    boolean REFRESH_OVER_SCROLL_BOUNCE_ENABLE = true;

    /**
     * 是否启用列表惯性滑动到底部时自动加载更多
     */
    boolean REFRESH_AUTO_LOAD_MORE_ENABLE = false;

    /**
     * 启动页时间
     */
    int START_ACTIVITY_DELAY = 2000;

    /**
     * 广告页时间
     */
    int AD_ACTIVITY_DELAY_SECOND = 5;

    /**
     * 图片上传缓存目录名
     */
    String IMG_UPLOAD_CACHE_DIR_NAME = "img_upload";

    /**
     * 图片file压缩缓存目录名
     */
    String IMG_COMPRESS_CACHE_DIR_NAME = "img_compress";

    /**
     * 下载文件的路径
     */
    String DOWNLOAD_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/baseproject";


    /**
     * 发送邮箱验证码的时间间隔
     */
    int SEND_E_MAIL_CODE_DELAY = 60;

    /**
     * 图片上传文件后缀名（不要更改，应该为合法的图片后缀）
     */
    String IMG_UPLOAD_NAME_EXTENSION = ".jpg";

    /**
     * 上传用户头像默认尺寸
     */
    int USER_ICON_UPLOAD_SIZE = 640;

    /**
     * 上传普通图片默认尺寸
     */
    int IMAGE_UPLOAD_SIZE = 1080;

    /**
     * 强制退出登陆状态的本地广播的Action
     */
    String ACTION_FORCE_OFFLINE = "com.dm.receiver.FORCE_OFFLINE";

    /**
     * 按钮不可点击状态的透明度
     */
    float ACTIVITY_BTN_UNCLICKABLE_ALPHA = 0.6f;

    /**
     * 主页双击退出时长间隔
     */
    long DOUBLE_BACK_PRESSED_TIME = 500L;

    /**
     * 软件版本强制升级，无法忽略版本
     */
    boolean APP_FORCE_UPDATE = false;

    /**
     * 软件版本忽略按钮倒计时时长
     */
    int APP_UPDATE_IGNORE_BTN_DURATION_SECOND = 0;

    /**
     * 软件版本更新同一版本最大忽略时间
     * 仅在{@link #APP_FORCE_UPDATE}为false时生效
     */
    long APP_UPDATE_IGNORE_TIME_MAX_MILL = 1 * 24 * 60 * 60 * 1000;

    /**
     * 登录注册界面按钮不可点击状态的透明度
     */
    float LOGIN_ACTIVITY_BTN_UNCLICKABLE_ALPHA = ACTIVITY_BTN_UNCLICKABLE_ALPHA;

    /**
     * 用户生日选择器启示日期
     */
    String USER_BIRTHDAY_DATE_PICKER_START = "1900-01-01 00:00";

    /**
     * 微信APP_ID
     */
    String WECHAT_APP_ID = "wx*********";


    /**
     * 历史搜索的存储大小
     */
    int HISTORICAL_SEARCH_SIZE = 10;
}