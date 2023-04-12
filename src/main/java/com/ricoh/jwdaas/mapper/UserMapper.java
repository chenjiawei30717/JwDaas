package com.ricoh.jwdaas.mapper;

import com.ricoh.jwdaas.dto.MyUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;



/** 
  * @Title: UserMapper.java 
  * @Copyright: Copyright (c) 2023 
  * @Company: RICOH
  * @version 1.0  
  * @author jwChen  
  * @date 2023年3月29日  
  */
@Mapper
public interface UserMapper {
	@Select("select * from my_user t where t.user_name = #{userName}")
	MyUser getUser(String userName);
}
