# WeChatPay-APIv3-Helper
> 微信支付V3商户进件示例
> 
> APIv3文档: https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/pages/applyment4sub.shtml
> 
> 提交申请单API: https://pay.weixin.qq.com/wiki/doc/apiv3/wxpay/tool/applyment4sub/chapter3_1.shtml
> 
> 如何查看证书序列号？
> 1. https://wechatpay-api.gitbook.io/wechatpay-api-v3/chang-jian-wen-ti/zheng-shu-xiang-guan#ru-he-cha-kan-zheng-shu-xu-lie-hao
> 2. 你可以在`src/main/java/com/langkye/api/paychl/wecahtpay/helper/WeChatPayUtilHelper.java`中进行查看使用示例。

**注意**：本项目是基于`gradle`进行构建, 如果你想使用maven进行构建, 那么：
1. 你可以安装gradle, 然后在本项目根目录下执行`gradle writeNewPom`，然后会在build目录下生生成`pom.xml`文件。
2. 本项目会将本地转换的`pom.xml`放在`src/main/resources/`下。

**如何在项目中使用使用？**
1. 如果本项目足够你使用，你只需要：

    a. 将本项目的jar包依赖到你的项目中。

    b. 在你的项目中按照`src/main/resources/wechatpay-config.properties`进行配置你的，商户证书、商户私钥、微信平台的证书等信息。
    
    c. 关于证书文件的存放位置，你可以定义，最终的位置请务必与配置文件`wechatpay-config.properties`进行对应！
2. 如果本项目不满足你的需求，你可能需要：
    按照自己的实际需求进行开发，然后可以参考上述进行使用。   
    *提示：最好先按照步骤1配置，并尝试测试功能是否能正常使用^_^。*
