package com.kplike.library.util;

public class PwdVallid {

	/**
	 * 
	 * 校验登录密码是否合法
	 * 
	 * <p>
	 * 
	 * 1. 6-20 个字符<br>
	 * 
	 * 2. 不能是纯数字<br>
	 * 
	 * 3. 不能是纯字母
	 * 
	 * </p>
	 * 
	 * 
	 * 
	 * @param password
	 * 
	 *            登录密码
	 * 
	 * @return 合法返回 true, 否则返回 false
	 */

	public static final boolean validLoginPwd(String password, String loginName) {
		// 6-20 个字符
		if (!password.matches("^.{6,20}$")) {
			return false;
		}
		// 不能是纯数字
		if (password.matches("^\\d+$")) {
			return false;
		}
		// 不能是纯字母
		if (password.toLowerCase().matches("^[a-z]+$")) {
			return false;
		}
		// 不能是纯符号
		if (password.matches("^[\\W_]+$")) {
			return false;
		}
		// 不能包含有连续四位及以上顺序(或逆序)数字或字母；（如：1234、abcd等）
		int asc = 1;
		int desc = 1;
		int lastChar = password.charAt(0);
		for (int i = 1; i < password.length(); i++) {
			int currentChar = password.charAt(i);
			if (!(password.charAt(i) + "").matches("[a-zA-Z0-9]")) {
				asc = 0;
				desc = 0;
			} else if (lastChar == currentChar - 1) {
				asc++;
				desc = 1;
			} else if (lastChar == currentChar + 1) {
				desc++;
				asc = 1;
			} else {
				asc = 1;
				desc = 1;
			}
			if (asc >= 4 || desc >= 4) {
				return false;
			}
			lastChar = currentChar;
		}
		// 密码中不能包含有连续四位及以上重复字符，字母不区分大小写；（如：8888、AAAA、$$$$等）
		if (password.toLowerCase().matches(".*(.)\\1{3,}.*")) {
			return false;
		}
		// 不能将帐号名作为密码的一部分存在于密码，帐号密码也不能一样
		if (!StringUtils.isEmpty(loginName)
				&& (loginName.equals(password) || password.contains(loginName))) {
			return false;
		}
		// 常用禁忌词不区分大小写不能作为密码的一部分存在于密码中
		if (password.toLowerCase().matches(".*(admin|pass).*")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 长度必须为6-20个字符
	 * @param password
	 * @param loginName
	 * @return
	 */
	public static final boolean validLoginPwd1(String password, String loginName) {
		// 6-20 个字符
		if (!password.matches("^.{6,20}$")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 密码不能为纯数字或纯字母
	 * @param password
	 * @param loginName
	 * @return
	 */
	public static final boolean validLoginPwd2(String password, String loginName) {
		// 不能是纯数字
		if (password.matches("^\\d+$")) {
			return false;
		}
		if (password.toLowerCase().matches("^[a-z]+$")) {
			return false;
		}
		if (password.matches("^[\\W_]+$")) {
			return false;
		}
		return true;
	}
	/**
	 * 密码不能包含四位重复数字或字母
	 * @param password
	 * @param loginName
	 * @return
	 */
	public static final boolean validLoginPwd3(String password, String loginName) {
		// 密码中不能包含有连续四位及以上重复字符，字母不区分大小写；（如：8888、AAAA、$$$$等）
		if (password.toLowerCase().matches(".*(.)\\1{3,}.*")) {
			return false;
		}
		return true;
	}
	/**
	 * 密码不能包含四位连续数字或字母
	 * @param password
	 * @param loginName
	 * @return
	 */
	public static final boolean validLoginPwd4(String password, String loginName) {
		// 不能包含有连续四位及以上顺序(或逆序)数字或字母；（如：1234、abcd等）
		int asc = 1;
		int desc = 1;
		int lastChar = password.charAt(0);
		for (int i = 1; i < password.length(); i++) {
			int currentChar = password.charAt(i);
			if (!(password.charAt(i) + "").matches("[a-zA-Z0-9]")) {
				asc = 0;
				desc = 0;
			} else if (lastChar == currentChar - 1) {
				asc++;
				desc = 1;
			} else if (lastChar == currentChar + 1) {
				desc++;
				asc = 1;
			} else {
				asc = 1;
				desc = 1;
			}
			if (asc >= 4 || desc >= 4) {
				return false;
			}
			lastChar = currentChar;
		}
		return true;
	}
	
	/**
	 * 密码不能包含账号信息
	 * @param password
	 * @param loginName
	 * @return
	 */
	public static final boolean validLoginPwd5(String password, String loginName) {
		// 不能将帐号名作为密码的一部分存在于密码，帐号密码也不能一样
		if (!StringUtils.isEmpty(loginName)
				&& (loginName.equals(password) || password.contains(loginName))) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * 校验支付密码是否合法. 注册, 修改或找回支付密码时的规则
	 * 
	 * 
	 * 
	 * @param password
	 * 
	 *            支付密码
	 * 
	 * @return 合法返回 true, 否则返回 false
	 */
	public static final boolean validNewPayPwd(String password, String loginName) {
		// 6-20 个字符
		if (!password.matches("^\\d{6,20}$")) {
			return false;
		}
		// 不能包含有连续四位及以上顺序(或逆序)数字或字母；（如：1234、abcd等）
		int asc = 1;
		int desc = 1;
		int lastChar = password.charAt(0);
		for (int i = 1; i < password.length(); i++) {
			int currentChar = password.charAt(i);
			if (!(password.charAt(i) + "").matches("[a-zA-Z0-9]")) {
				asc = 0;
				desc = 0;
			} else if (lastChar == currentChar - 1) {
				asc++;
				desc = 1;
			} else if (lastChar == currentChar + 1) {
				desc++;
				asc = 1;
			} else {
				asc = 1;
				desc = 1;
			}
			if (asc >= 4 || desc >= 4) {
				return false;
			}
			lastChar = currentChar;
		}

		// 密码中不能包含有连续四位及以上重复字符，字母不区分大小写；（如：8888、AAAA、$$$$等）
		if (password.toLowerCase().matches(".*(.)\\1{3,}.*")) {
			return false;
		}

		// 不能将帐号名作为密码的一部分存在于密码，帐号密码也不能一样
		if (!StringUtils.isEmpty(loginName)
				&& (loginName.equals(password) || password.contains(loginName))) {
			return false;
		}
		return true;
	}
	/**
	 * 
	 * 校验支付密码是否合法. 注册, 修改或找回支付密码时的规则
	 * 
	 * 
	 * 
	 * @param password
	 * 
	 *            支付密码
	 * 
	 * @return 合法返回 true, 否则返回 false
	 */
	public static final boolean validPayPwd(String password, String loginName) {
//		// 6-20 个字符
//		if (!password.matches("^\\d{6,20}$")) {
//			return false;
//		}
		// 不能包含有连续四位及以上顺序(或逆序)数字或字母；（如：1234、abcd等）
		int asc = 1;
		int desc = 1;
		int lastChar = password.charAt(0);
		for (int i = 1; i < password.length(); i++) {
			int currentChar = password.charAt(i);
			if (!(password.charAt(i) + "").matches("[0-9]")) {
				asc = 0;
				desc = 0;
			} else if (lastChar == currentChar - 1) {
				asc++;
				desc = 1;
			} else if (lastChar == currentChar + 1) {
				desc++;
				asc = 1;
			} else {
				asc = 1;
				desc = 1;
			}
			if (asc >= 4 || desc >= 4) {
				return false;
			}
			lastChar = currentChar;
		}
		
		// 密码中不能包含有连续四位及以上重复字符，字母不区分大小写；（如：8888、AAAA、$$$$等）
		if (password.toLowerCase().matches(".*(.)\\1{3,}.*")) {
			return false;
		}
		
		// 不能将帐号名作为密码的一部分存在于密码，帐号密码也不能一样
		if (!StringUtils.isEmpty(loginName)
				&& (loginName.equals(password) || password.contains(loginName))) {
			return false;
		}
		return true;
	}

}
