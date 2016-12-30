# ThinkFuture：BasePlatform为基础类库，app为使用类库样例APP<br/>
> Android快速开发基础类库<br/>
    使用最新的Android7.1开发平台开发，简洁无冗余，由实际项目总结所得。大大简化了各种公用组件的使用，使APP的开发层更关注
    业务。<br/>
    特点有：</br>
    1、封装了网络请求，可设置关闭activity时是否取消请求。默认是取消所有正在进行的请求。</br>
    2、封装了本地缓存，有数据库本地存储配置(替代SharePreference)、文件缓存（使用了DiskLruCache)</br>
    3、内置有下拉刷新，上拉加载功能，支持基本所有控件。同时有缺省无数据的页面（可定制）</br>
    4、封装了RecyclerView、ListView的Adapter，使其使用更方便</br>
    5、封装了Android 6.0之后的权限请求</br>
    6、封装了组件之间的总线消息传递，重写一个方法搞定。可不使用Broadcast啦</br>
    7、封装了activity之间的跳转</br>
    8、封装了选择本地图片、裁剪图片、浏览大图功能</br>
    9、封装了Dialog，解决多次弹出问题</br>
    封装了网络请求、图片显示、本地缓存</br>
> 使用的第三方库有<br/>
    1、网络请求、图片加载：xUtils3，<https://github.com/wyouflf/xUtils3><br/>
    2、JSON解析库：FastJson，<https://github.com/alibaba/fastjson><br/>
    3、Log工具：LogUtils，<https://github.com/Jude95/SwipeBackHelper><br/>
    4、右滑关闭Activity：SwipeBackHelper，<https://github.com/pengwei1024/LogUtils><br/>
    5、总线消息传递：EventBus，<https://github.com/greenrobot/EventBus><br/>
