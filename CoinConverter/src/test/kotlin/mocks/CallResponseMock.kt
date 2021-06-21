package mocks

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CallResponseMock<T>(ResponseMocked: T) : Call<T> {

    private val responseMocked = ResponseMocked

    override fun clone(): Call<T> {
        return this
    }

    override fun execute(): Response<T> {
        return Response.success(responseMocked)
    }

    override fun enqueue(callback: Callback<T>) {
        println("enqueue $callback")
    }

    override fun isExecuted(): Boolean {
        return false
    }

    override fun cancel() {
        println("cancel")
    }

    override fun isCanceled(): Boolean {
        return false
    }

    override fun request(): Request {
        return Request.Builder().build()
    }

    override fun timeout(): Timeout {
        println("timeout")
        return Timeout()
    }

}