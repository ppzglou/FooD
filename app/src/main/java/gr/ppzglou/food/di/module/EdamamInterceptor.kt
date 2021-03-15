package gr.ppzglou.food.di.module

import okhttp3.Interceptor
import okhttp3.Response


class EdamamInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("app_key", "35ed14464455d5c25e47d3d28a793dc3")
            .addQueryParameter("app_id", "7c5f9b91")
            .build()

        val requestBuilder = original.newBuilder()

        val request = requestBuilder.url(url).build()
        return chain.proceed(request)
    }

}