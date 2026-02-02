package web.ssm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import web.ssm.dto.DeptDTO;
import web.ssm.dto.OpResultDTO;
import web.ssm.service.DeptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping(value = "/api/dept")
public class DeptController {

    private final DeptService deptService;

    @Autowired
    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    private final Logger logger = LoggerFactory.getLogger(DeptController.class);

    /**
     * @Description: 获取所有部门列表
     * @Author: Song
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public OpResultDTO listAll() {
        OpResultDTO opResult = new OpResultDTO();
        try {
            List<DeptDTO> list = deptService.listAll();
            opResult.setMsgResult("success");
            opResult.setObjResult(list);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 根据ID获取部门
     * @Author: Song
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public OpResultDTO getById(@RequestParam Integer deptId) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            DeptDTO dept = deptService.getById(deptId);
            opResult.setMsgResult("success");
            opResult.setObjResult(dept);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 添加部门
     * @Author: Song
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public OpResultDTO add(@RequestBody DeptDTO deptDTO) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            opResult = deptService.add(deptDTO);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 更新部门
     * @Author: Song
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public OpResultDTO update(@RequestBody DeptDTO deptDTO) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            opResult = deptService.update(deptDTO);
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }

    /**
     * @Description: 删除部门
     * @Author: Song
     */
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public OpResultDTO remove(@RequestBody DeptDTO deptDTO) {
        OpResultDTO opResult = new OpResultDTO();
        try {
            opResult = deptService.remove(deptDTO.getDeptId());
        } catch (Exception e) {
            logger.error(e.toString());
            opResult.setMsgResult("error");
        }
        return opResult;
    }
}



