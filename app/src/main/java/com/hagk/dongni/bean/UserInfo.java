package com.hagk.dongni.bean;

/**
 * 用户的信息JavaBean
 * @author Administrator
 *
 */
public class UserInfo {
	/**
     * code : 200
     * more : /zixun/list_2.json
     * userinfo : {
     * 		"userimage":"http://posts.cdn.wallstcn.com/73/55/b5/20151118145843-2b447be3.jpg",
     * 		"nickname":"nickname",
     * 		"describe":"用户A",
     * 		"status":"在线"
     * }
     */

    private int code;
    private String more;
    private UserBean userinfo;
    
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
	}

	public UserBean getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(UserBean userinfo) {
		this.userinfo = userinfo;
	}

	public static class UserBean {
    	private String userimage;
    	private String nickname;
    	private String describe;
    	private String status;
		public String getUserimage() {
			return userimage;
		}
		public void setUserimage(String userimage) {
			this.userimage = userimage;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getDescribe() {
			return describe;
		}
		public void setDescribe(String describe) {
			this.describe = describe;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
    	
    }
}
