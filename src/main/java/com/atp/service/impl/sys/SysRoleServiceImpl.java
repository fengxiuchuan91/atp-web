package com.atp.service.impl.sys;

import java.util.List;
import java.util.Objects;

import com.atp.dto.base.response.BasePageResponse;
import com.atp.dto.member.AtpMemCourseDTO;
import com.atp.exception.ATPException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.atp.entity.sys.SysRole;
import com.atp.dto.sys.SysRoleDTO;
import com.atp.service.sys.SysRoleService;
import com.atp.dao.sys.SysRoleDao;
/**
 * @Description: SysRoleService 实现类
 * @author: fengxiuchuan
 * @date: 2018-08-01 17:57:46
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleServiceImpl implements SysRoleService {

    @Autowired
    private SysRoleDao sysRoleDao;

    @Override
    @Transactional(readOnly = true)
    public SysRole getById(Long id) throws ATPException {
        if (id == null) {
            return null;
        }
        return sysRoleDao.selectByPrimaryKey(id);
    }
    

    @Override
    @Transactional(readOnly = true)
    public List<SysRole> queryList(SysRoleDTO dto)  throws ATPException{
        if (dto == null) {
            return null;
        }
        return sysRoleDao.queryList(dto);
    }
    


    @Override
    public int save(SysRole record)  throws ATPException{
        if (record == null) {
            return 0;
        }
        return sysRoleDao.save(record);
    }

    @Override
    public int saveBatch(List<SysRole> recordList)  throws ATPException{
        if (CollectionUtils.isEmpty(recordList)) {
            return 0;
        }
        return sysRoleDao.saveBatch(recordList);
    }

    @Override
    public int updateById(SysRole record)  throws ATPException{
        if (record == null) {
            return 0;
        }
        return sysRoleDao.updateByPrimaryKey(record);
    }

    @Override
    public int deleteById(Long id)  throws ATPException{
        if (id == null) {
            return 0;
        }
        return sysRoleDao.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteBatchByIds(List<Long> ids)  throws ATPException{
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        return sysRoleDao.deleteBatchByIds(ids);
    }

    @Override
    public BasePageResponse<SysRoleDTO> queryAllList(SysRoleDTO roleDTO) throws ATPException {
        Page<Object> page = PageHelper.startPage(roleDTO.getPage(), roleDTO.getPageSize(), StringUtils.isBlank(roleDTO.getOrderBy()) ? "":roleDTO.getOrderBy());
        List<SysRoleDTO> list = sysRoleDao.queryAllList(roleDTO);
        BasePageResponse<SysRoleDTO> response = new BasePageResponse<SysRoleDTO>();


        if (list != null) {
            response.setRows(list);
            response.setTotal((int) page.getTotal());
            response.setTotalPage(roleDTO.getPageSize());
        }

        return response;
    }

    @Override
    public void addRole(SysRole sysRole) throws ATPException {
        //1 校验
        validateAddForm(sysRole);
        //2 保存
        sysRoleDao.save(sysRole);
    }


    private void validateAddForm(SysRole sysRole) throws ATPException{
        if(Objects.isNull(sysRole)){
            throw new ATPException("请求参数不合法");
        }
        if(StringUtils.isBlank(sysRole.getRoleCode()) || StringUtils.isBlank(sysRole.getRoleName())){
            throw new ATPException("请填写角色编号和名称");
        }

        //唯一性校验
        List<SysRoleDTO> roleList = sysRoleDao.queryRoleListByCode(sysRole.getRoleCode(),sysRole.getId());
        if(CollectionUtils.isNotEmpty(roleList)){
            throw new ATPException("角色编码是唯一的");
        }
    }
    @Override
    public void editRole(SysRole sysRole) throws ATPException {
        //1 校验
        validateAddForm(sysRole);
        //2 更新
        sysRoleDao.updateByPrimaryKeySelective(sysRole);
    }

    @Override
    public void delRole(Long id) throws ATPException {
        if(Objects.isNull(id)){
            throw new ATPException("非法的请求");
        }
        sysRoleDao.deleteByPrimaryKey(id);
    }
}