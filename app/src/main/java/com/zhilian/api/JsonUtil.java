package com.zhilian.api;

import com.google.gson.Gson;
import com.zhilian.hzrf_oa.entity.AddressList;
import com.zhilian.hzrf_oa.entity.NotepadBeen;
import com.zhilian.hzrf_oa.entity.OpinionBeen;
import com.zhilian.hzrf_oa.entity.SelectPerson;
import com.zhilian.hzrf_oa.json.MailDetail;
import com.zhilian.hzrf_oa.json.PersonnalADepartment;
import com.zhilian.hzrf_oa.json.ReceiveMailList;
import com.zhilian.hzrf_oa.json.SchduleList;
import com.zhilian.hzrf_oa.json.T_Achieve;
import com.zhilian.hzrf_oa.json.T_Announce;
import com.zhilian.hzrf_oa.json.T_Department;
import com.zhilian.hzrf_oa.json.T_FJList;
import com.zhilian.hzrf_oa.json.T_InnerSend;
import com.zhilian.hzrf_oa.json.T_Receive;
import com.zhilian.hzrf_oa.json.T_Record;
import com.zhilian.hzrf_oa.json.T_Selectman;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/12/6.
 */
public class JsonUtil {
	public static final ObjectMapper mapper = new ObjectMapper();

	public static void main(String[] args) throws Exception{
		JavaType javaType = getCollectionType(ArrayList.class, T_Announce.class);
		List<T_Announce> lst =  (List<T_Announce>)mapper.readValue("", javaType);
	}
	/**
	 * 获取泛型的Collection Type
	 * @param collectionClass 泛型的Collection
	 * @param elementClasses 元素类
	 * @return JavaType Java类型
	 * @since 1.0
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	public static List<T_Announce> getAnnounceList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_Announce.class);
		List<T_Announce> list = new ArrayList<>();
		try {
			list =  (List<T_Announce>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<T_Receive> getReceiveList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_Receive.class);
		List<T_Receive> list = new ArrayList<>();
		try {
			list =  (List<T_Receive>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<T_Department> getDepartmentList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_Department.class);
		List<T_Department> list = new ArrayList<>();
		try {
			list =  (List<T_Department>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<AddressList> getPersonalsList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, AddressList.class);
		List<AddressList> list = new ArrayList<>();
		try {
			list =  (List<AddressList>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<T_FJList> getFJList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_FJList.class);
		List<T_FJList> list = new ArrayList<>();
		try {
			list =  (List<T_FJList>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<OpinionBeen> getOpinionList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, OpinionBeen.class);
		List<OpinionBeen> list = new ArrayList<>();
		try {
			list =  (List<OpinionBeen>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<T_Record> getRecordList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_Record.class);
		List<T_Record> list = new ArrayList<>();
		try {
			list =  (List<T_Record>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<SchduleList> getSchduleList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, SchduleList.class);
		List<SchduleList> list = new ArrayList<>();
		try {
			list =  (List<SchduleList>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<T_Selectman> getselectmanList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_Selectman.class);
		List<T_Selectman> list = new ArrayList<>();
		try {
			list =  (List<T_Selectman>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<PersonnalADepartment> getPersonnalADepartment(String json){
		JavaType javaType = getCollectionType(ArrayList.class, PersonnalADepartment.class);
		List<PersonnalADepartment> list = new ArrayList<>();
		try {
			list =  (List<PersonnalADepartment>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<T_Achieve> getAchieveList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_Achieve.class);
		List<T_Achieve> list = new ArrayList<>();
		try {
			list =  (List<T_Achieve>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<ReceiveMailList> getReceiveMailList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, ReceiveMailList.class);
		List<ReceiveMailList> list = new ArrayList<>();
		try {
			list =  (List<ReceiveMailList>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<NotepadBeen> getNoteList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, NotepadBeen.class);
		List<NotepadBeen> list = new ArrayList<>();
		try {
			list =  (List<NotepadBeen>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<SelectPerson> getPersonnalInfo(String json){
		JavaType javaType = getCollectionType(ArrayList.class, SelectPerson.class);
		List<SelectPerson> list = new ArrayList<>();
		try {
			list =  (List<SelectPerson>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static List<MailDetail> getMailInfo(String json){
		JavaType javaType = getCollectionType(ArrayList.class, MailDetail.class);
		List<MailDetail> list = new ArrayList<>();
		try {
			list =  (List<MailDetail>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}


	public static List<T_InnerSend> getInnerSendList(String json){
		JavaType javaType = getCollectionType(ArrayList.class, T_InnerSend.class);
		List<T_InnerSend> list = new ArrayList<>();
		try {
			list =  (List<T_InnerSend>)mapper.readValue(json, javaType);
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}

	public static <T> List<T> getObjectList(String jsonStr,Class<T> clazz){
		JavaType javaType = getCollectionType(ArrayList.class,clazz);
		List<T> list = new ArrayList<T>();
		try {
			list = mapper.readValue(jsonStr,javaType);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			return list;
		}
	}
	public static <T> List<T> getObjectList1(String jsonStr, Class<T[]> clazz){
		return Arrays.asList(new Gson().fromJson(jsonStr,clazz));
	}
}
