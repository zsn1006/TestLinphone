package org.linphone.newphone.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class HanziToPinyin {

	private static HanziToPinyin instance = null;

	private HanziToPinyin() {
	}

	public static HanziToPinyin getInstance() {
		if (instance == null) {
			load();
		}
		return instance;
	}
	
	private static synchronized void load() {
		if (instance == null) {
			instance = new HanziToPinyin();
		}
	}
	
	public String toPinYin(String name){
		
        HanyuPinyinOutputFormat hanyuPinyin = new HanyuPinyinOutputFormat();
        /***大写*/
        hanyuPinyin.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        /***无音调*/
        hanyuPinyin.setToneType(HanyuPinyinToneType.WITH_TONE_MARK);
        /***'¨¹' is "v"*/
        hanyuPinyin.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        
        String code = "";
        String[] pinyinArray=null;
        for (int i=0; i<name.length(); i++) {
        	char hanzi = name.charAt(i);
        	try {
        		/***是否在汉字范围内*/
                if(hanzi>=0x4e00 && hanzi<=0x9fa5){
                    pinyinArray = PinyinHelper.toHanyuPinyinStringArray(hanzi, hanyuPinyin);
                    code += pinyinArray[0].substring(0, 1);
                } else if (hanzi>=0x61 && hanzi<=0x7a) {
                	code += String.valueOf((char)(hanzi-32));
                } else if (hanzi>=0x41 && hanzi<=0x5a) {
                	code += hanzi;
                } else {
                	code += "#";
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }        	
        }

        return code;
    }
}
