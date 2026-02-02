package web.ssm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.ssm.dto.DeptDTO;
import web.ssm.dto.OpResultDTO;
import web.ssm.entity.DeptEntity;
import web.ssm.mapper.DeptMapper;
import web.ssm.service.DeptService;

import java.util.Date;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    private final DeptMapper deptMapper;

    @Autowired
    public DeptServiceImpl(DeptMapper deptMapper) {
        this.deptMapper = deptMapper;
    }

    @Override
    public List<DeptDTO> listAll() throws Exception {
        return deptMapper.listAll();
    }

    @Override
    public DeptDTO getById(Integer deptId) throws Exception {
        return deptMapper.getById(deptId);
    }

    @Override
    public OpResultDTO add(DeptDTO deptDTO) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        DeptEntity deptEntity = new DeptEntity();
        deptEntity.setDeptName(deptDTO.getDeptName());
        deptEntity.setDeptDesc(deptDTO.getDeptDesc());
        deptEntity.setGmtCreate(new Date());
        deptEntity.setGmtModified(new Date());
        
        deptMapper.add(deptEntity);
        opResult.setMsgResult("success");
        opResult.setObjResult(deptEntity.getDeptId());
        return opResult;
    }

    @Override
    public OpResultDTO update(DeptDTO deptDTO) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        DeptEntity deptEntity = new DeptEntity();
        deptEntity.setDeptId(deptDTO.getDeptId());
        deptEntity.setDeptName(deptDTO.getDeptName());
        deptEntity.setDeptDesc(deptDTO.getDeptDesc());
        deptEntity.setGmtModified(new Date());
        
        deptMapper.update(deptEntity);
        opResult.setMsgResult("success");
        opResult.setObjResult("更新成功");
        return opResult;
    }

    @Override
    public OpResultDTO remove(Integer deptId) throws Exception {
        OpResultDTO opResult = new OpResultDTO();
        deptMapper.remove(deptId);
        opResult.setMsgResult("success");
        opResult.setObjResult("删除成功");
        return opResult;
    }
}



