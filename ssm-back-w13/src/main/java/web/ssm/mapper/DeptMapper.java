package web.ssm.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import web.ssm.dto.DeptDTO;
import web.ssm.entity.DeptEntity;

import java.util.List;

@Mapper
@Repository
public interface DeptMapper {

    /**
     * @Description: 获取所有部门列表
     * @Author: Song
     */
    @Select("SELECT dept_id, dept_name, dept_desc, gmt_create, gmt_modified FROM tb_dept ORDER BY dept_id")
    List<DeptDTO> listAll() throws Exception;

    /**
     * @Description: 根据ID获取部门信息
     * @Author: Song
     */
    @Select("SELECT dept_id, dept_name, dept_desc, gmt_create, gmt_modified FROM tb_dept WHERE dept_id = #{deptId}")
    DeptDTO getById(@Param("deptId") Integer deptId) throws Exception;

    /**
     * @Description: 添加部门
     * @Author: Song
     */
    @Insert("INSERT INTO tb_dept (dept_name, dept_desc, gmt_create, gmt_modified) " +
            "VALUES (#{deptEntity.deptName}, #{deptEntity.deptDesc}, #{deptEntity.gmtCreate}, #{deptEntity.gmtModified})")
    @Options(useGeneratedKeys = true, keyProperty = "deptEntity.deptId", keyColumn = "dept_id")
    Integer add(@Param("deptEntity") DeptEntity deptEntity) throws Exception;

    /**
     * @Description: 更新部门
     * @Author: Song
     */
    @Update("UPDATE tb_dept SET dept_name = #{deptEntity.deptName}, dept_desc = #{deptEntity.deptDesc}, " +
            "gmt_modified = #{deptEntity.gmtModified} WHERE dept_id = #{deptEntity.deptId}")
    Integer update(@Param("deptEntity") DeptEntity deptEntity) throws Exception;

    /**
     * @Description: 删除部门
     * @Author: Song
     */
    @Delete("DELETE FROM tb_dept WHERE dept_id = #{deptId}")
    Integer remove(@Param("deptId") Integer deptId) throws Exception;
}



