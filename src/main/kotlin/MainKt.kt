import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.requests.GatewayIntent
import javax.security.auth.login.LoginException
import kotlin.system.exitProcess

class MainKt : ListenerAdapter() {
    private val TOKEN = "" //Write BotToken
    private val COMMAND_PREFIX = "!"

    // throwsでやると、jarにした時見つからなくてエラーはくっぽい？
    fun first() {
        try {
            //Login
            JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                .addEventListeners(Main())
                .setActivity(Activity.playing("作業"))
                .build()
        } catch (e: LoginException) {
            e.printStackTrace()
        }
    }

    //メッセージ受け取った時に呼ばれるやつ
    override fun onMessageReceived(event: MessageReceivedEvent) {
        //打たれたメッセージ取得
        val msg = event.message
        //チャットが打たれたチャンネル取得
        val channel = event.channel

        when(msg.contentRaw){
            COMMAND_PREFIX + "hello" -> { //!helloの時
                //メッセージ送信
                channel.sendMessage(event.member!!.effectiveName + "さん、こんにちは！").queue()

                val time = System.currentTimeMillis()
                channel.sendMessage(event.member.toString() + "さん、こんにちは！") /* => RestAction<Message> */
                    .queue { response: Message ->
                        response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue()
                    }
            }

            COMMAND_PREFIX + "exit" -> { //!exitの時
                channel.sendMessage("プログラムを終了します。").queue()
                exitProcess(0)
            }
        }

    }
}

fun main(){
    MainKt().first()
}