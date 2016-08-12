使用Retrofit的原因是他是非常不错REST API 库,而且对RxJava 支持的。
使用RxJava 可以有效的使代码的可读性大大的提高,而且相当的优雅。
Package Http 下面主要是result对应及result对象的处理(配合RxJava .map一个httpResultFunc 对象)
Package Progress Loading 的相关类
package Subscribers  就是对Subscriber做了一些基础的封装 ,如加载时Loading... 等 。

数据解析继续使用fastJson  已经有FastJsonConverterFactory 的库了   .之前我一直用的Gson

具体使用
retrofit :http://square.github.io/retrofit/
RxJava : http://gank.io/post/560e15be2dca930e00da1083