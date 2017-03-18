package command;

import org.json.JSONArray;
import tool.ConfigSystem;
import tool.Response;
import util.GroupMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Eldath on 2017/1/29 0029.
 *
 * @author Eldath
 */
public class XiaoIce extends GroupMessageCommand {
    // private static final Logger logger = LoggerFactory.getLogger(XiaoIce.class);
    private static XiaoIce instance = null;
    static Map<Long, Integer> blackList = new HashMap<>();
    private static String[] blockList = getBlockList();

    public static XiaoIce getInstance() {
        if (instance == null) instance = new XiaoIce();
        return instance;
    }

    private static String[] getBlockList() {
        JSONArray array = (JSONArray) ConfigSystem.getInstance()
                .getCommandConfig("XiaoIce", "BlockList_Words");
        String[] strings = new String[array.length()];
        for (int i = 0; i < array.length(); i++)
            strings[i] = array.getString(i);
        return strings;
    }

    @Override
    public void doPost(GroupMessage message) {
        long group_uid = message.getGroupUid();
        String sender = message.getSenderNickName();
        long sender_uid = message.getSenderUid();
        String content = message.getContent()
                .trim()
                .toLowerCase()
                .replaceAll("[\\pP\\p{Punct}]", "");
        String text = content;
        text = text.replaceAll(getKeyWordRegex().toString(), "");
        if ("".equals(text.replace(" ", ""))) {
            Response.responseGroup(group_uid, "@\u2005" + sender +
                    " 消息不能为空哦~(*∩_∩*)");
            return;
        }
        boolean blockListEnabled = (boolean) ConfigSystem.getInstance()
                .getCommandConfig("XiaoIce", "Uid_BlackList_Enabled");
        if (blockListEnabled) {
            blackList.put(sender_uid, 0);
            if (blackList.containsKey(sender_uid))
                if (blackList.get(sender_uid) > 2) {
                    Response.responseGroup(group_uid, "@\u2005" + sender +
                            " 您的帐号由于发送过多指令或不允许关键字，现已被屏蔽~o(╯□╰)o！");
                    return;
                }
        }
        if (strIsEnglish(text)) {
            if (text.length() < 5) {
                Response.responseGroup(group_uid, "@\u2005" + sender + " 您的消息过短~o(╯□╰)o！");
                return;
            }
        } else if (text.length() < 3) {
            Response.responseGroup(group_uid, "@\u2005" + sender + " 您的消息过短~o(╯□╰)o！");
            return;
        }
        for (String thisBlockString : blockList)
            if (content.replace(" ", "").contains(thisBlockString)) {
                String notice = "您发送的消息含有不允许的关键词！";
                if (blockListEnabled) {
                    notice = "您发送的消息含有不允许的关键词，注意：3次发送不允许关键词后帐号将被屏蔽！⊙﹏⊙!";
                    blackListPlus(sender_uid);
                }
                Response.responseGroup(group_uid, "@\u2005" + sender + " " + notice);
                return;
            }
        content = content.replaceAll(getKeyWordRegex().toString(), "小冰");
        String responseXiaoIce = Response.responseXiaoIce(content);
        if (responseXiaoIce == null) return;
        Response.responseGroup(group_uid, "@\u2005" + sender + " " + responseXiaoIce);
    }

    private boolean strIsEnglish(String word) {
        for (int i = 0; i < word.length(); i++)
            if (!(word.charAt(i) >= 'A' && word.charAt(i) <= 'Z')
                    && !(word.charAt(i) >= 'a' && word.charAt(i) <= 'z'))
                return false;
        return true;
    }

    private void blackListPlus(long sender_uid) {
        int pastValue;
        pastValue = blackList.get(sender_uid);
        blackList.put(sender_uid, ++pastValue);
    }

    @Override
    public String getHelpMessage() {
        return "avalon answer me / 阿瓦隆回答我 / avalon tell me / 阿瓦隆告诉我：激活智能回复功能";
    }

    @Override
    public Pattern getKeyWordRegex() {
        return Pattern.compile("avalon answer me |阿瓦隆回答我 |avalon tell me |阿瓦隆告诉我 ");
    }
}
