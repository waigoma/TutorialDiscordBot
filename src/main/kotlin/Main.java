import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main extends ListenerAdapter {
    private static final String TOKEN = ""; //Write BotToken
    private static final String COMMAND_PREFIX = "!";

    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    // throwsでやると、jarにした時見つからなくてエラーはくっぽい？
    public static void main(String[] args) {
        try {
            //Login
            JDABuilder.createLight(TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                    .addEventListeners(new Main())
                    .setActivity(Activity.playing("作業"))
                    .build();
        }catch (LoginException e){
            e.printStackTrace();
        }

    }

    //メッセージ受け取った時に呼ばれるやつ
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        //打たれたメッセージ取得
        Message msg = event.getMessage();
        //チャットが打たれたチャンネル取得
        MessageChannel channel = event.getChannel();

        switch (msg.getContentRaw()){
            case COMMAND_PREFIX + "hello": //!helloの時
                //メッセージ送信
                channel.sendMessage(event.getMember().getEffectiveName() + "さん、こんにちは！").queue();

                channel.sendMessage(event.getMember() + "さん、こんにちは！") /* => RestAction<Message> */
                        .queue(response /* => Message */ -> {
                            response.editMessageFormat("現在時刻は " + format.format(Calendar.getInstance().getTime()) + "です。").queue();
                        });
                break;

            case COMMAND_PREFIX + "exit": //!exitの時
                channel.sendMessage("プログラムを終了します。").queue();
                long time = System.currentTimeMillis();

                while (exiting(time));
        }

    }

    private boolean exiting(long time){
        if (System.currentTimeMillis() - time > 500) System.exit(0);
        return true;
    }
}
