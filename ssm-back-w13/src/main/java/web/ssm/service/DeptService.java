package web.ssm.service;

import web.ssm.dto.DeptDTO;
import web.ssm.dto.OpResultDTO;

import java.util.List;

public interface DeptService {
    /**
     * @Description: 获取所有部门列表
     * @Author: Song
     */
    List<DeptDTO> listAll() throws Exception;

    /**
     * @Description: 根据ID获取部门
     * @Author: Song
     */
    DeptDTO getById(Integer deptId) throws Exception;

    /**
     * @Description: 添加部门
     * @Author: Song
     */
    OpResultDTO add(DeptDTO deptDTO) throws Exception;

    /**
     * @Description: 更新部门
     * @Author: Song
     */
    OpResultDTO update(DeptDTO deptDTO) throws Exception;

    /**
     * @Description: 删除部门
     * @Author: Song
     */
    OpResultDTO remove(Integer deptId) throws Exception;
}



