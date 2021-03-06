package avalon.group

import avalon.tool.pool.Variables
import avalon.util.GroupConfig
import avalon.util.GroupMessage
import java.util.*
import java.util.regex.Pattern

object Mo : GroupMessageResponder() {
	private val responseMessages = arrayOf(
			"哈哈蛤哈",
			"你们有没有... ...就是那种... ...那种... ...诗？",
			"那首诗怎么念来着？苟利国家... ...",
			"我跟你江，你们这是要负泽任的，民白不？",
			"还是去弹夏威夷吉他吧！",
			"枸杞有养生功效，古人云：枸利果佳生食宜，气阴火服必驱之",
			"下面我们有请州长夫人演唱！",
			"其实我和他想法一样，我也觉得一个城市的交响乐水平标志着这个城市的文明程度。",
			"我觉得，所有人，不论长幼都必须会说英语。",
			"世界语我知道，很好。我叔叔也曾经学习过世界语。",
			"送你们一个......鸡啊！明年是鸡年啊！",
			"I'm sorry. I am an electrical power engineer.",
			"没有……没任何（内定、钦点）的意思。还是按照香港的……按照基本法、按照选举的法——去产生……",
			"你们啊，不要想……喜欢……弄个大新闻，说现在已经钦定了，再把我批判一番。",
			"你们啊，naïve!（幼稚！）",
			"I'm angry!（我生气了！）")
	private val randomInstance = Random(System.currentTimeMillis())

	override fun doPost(message: GroupMessage, groupConfig: GroupConfig) {
		if (Variables.Mo_Reach_Max) return
		if (Variables.Mo_Count >= 30) {
			message.response("哼！你们今天膜的太多啦！长者肯定会生气的！")
			Variables.Mo_Reach_Max = true
			return
		}
		message.response(responseMessages[randomInstance.nextInt(responseMessages.size)])
		Variables.Mo_Count += 1
	}

	override fun responderInfo(): ResponderInfo =
			ResponderInfo(
					Pair("膜*关键词", "随机触发膜*语句"),
					Pattern.compile("\\w?(\\+1s|-1s|膜蛤|苟|续命|州长夫人|naive|江化)\\w?"),
					availableLocale = *arrayOf(Locale.SIMPLIFIED_CHINESE, Locale.CHINESE)
			)

	override fun instance() = this
}