package com.product.trans.util;

import com.weixin.constant.KeyConstant;
import com.weixin.util.ChineseHelper;
import com.weixin.util.MD5;
import com.weixin.util.RandomUtil;
import com.weixin.util.WeiXinUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public class TransUtil {
	
	/**
	 * 翻译结果
	 * @param source
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String translate(String source) throws ParseException, IOException{
		String to = "zh";
		if (source.contains("=")){
			to = source.split("=")[0];
			source = source.split("=")[1];
		} else {
			if (ChineseHelper.hasChinese(source)){
				to = "en";
			}
		}
		String salt = RandomUtil.getRandom(10);
		String sign = MD5.md5(KeyConstant.BAIDU_TRANS_APPID + source + salt + KeyConstant.BAIDU_TRANS_AK);
		String url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=KEYWORD&from=auto&to=" + to +
				"&appid=" + KeyConstant.BAIDU_TRANS_APPID + "&salt=" + salt + "&sign=" + sign;
		//String url = "http://openapi.baidu.com/public/2.0/translate/dict/simple?client_id="+KeyConstant.BAIDU_AK+"&q=KEYWORD&from=auto&to=auto";
		url = url.replace("KEYWORD", URLEncoder.encode(source, "UTF-8"));
		JSONObject jsonObject = WeiXinUtil.doGetStr(url);
		String result = "翻译失败，请重新输入。";
		if (!jsonObject.containsKey("error_code")){
			JSONArray array = jsonObject.getJSONArray("trans_result");
			JSONObject resultJson = array.getJSONObject(0);
			result = resultJson.getString("dst");
		} else {
			result = result + "错误码：" + jsonObject.getInt("error_code");
		}
//		StringBuffer dst = new StringBuffer();
//		if("0".equals(errno) && !"[]".equals(obj.toString())){
//			TransResult transResult = (TransResult) JSONObject.toBean(jsonObject, TransResult.class);
//			Data data = transResult.getData();
//			Symbols symbols = data.getSymbols()[0];
//			String phzh = symbols.getPh_zh()==null ? "" : "中文拼音："+symbols.getPh_zh()+"\n";
//			String phen = symbols.getPh_en()==null ? "" : "英式英标："+symbols.getPh_en()+"\n";
//			String pham = symbols.getPh_am()==null ? "" : "美式英标："+symbols.getPh_am()+"\n";
//			dst.append(phzh+phen+pham);
//
//			Parts[] parts = symbols.getParts();
//			String pat = null;
//			for(Parts part : parts){
//				pat = (part.getPart()!=null && !"".equals(part.getPart())) ? "["+part.getPart()+"]" : "";
//				String[] means = part.getMeans();
//				dst.append(pat);
//				for(String mean : means){
//					dst.append(mean+";");
//				}
//				dst.append("\n");
//			}
//		}else{
//			dst.append(translateFull(source));
//		}
		return result;
	}
	
	/**
	 * 翻译整句
	 * @param source
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	@Deprecated
	public static String translateFull(String source) throws ParseException, IOException{
		String url = "http://openapi.baidu.com/public/2.0/bmt/translate?client_id="+KeyConstant.BAIDU_AK+"&q=KEYWORD&from=auto&to=auto";
		url = url.replace("KEYWORD", URLEncoder.encode(source, "UTF-8"));
		JSONObject jsonObject = WeiXinUtil.doGetStr(url);
		StringBuffer dst = new StringBuffer();
		List<Map> list = (List<Map>) jsonObject.get("trans_result");
		for(Map map : list){
			dst.append(map.get("dst"));
		}
		return dst.toString();
	}
	
	public static void main(String[] args) throws ParseException, IOException {
		System.out.println(translate("en=我们是一家人"));
	}

}
