package com.eip.common.web.base;

import com.eip.common.core.core.assertion.enums.BusinessResponseEnum;
import com.eip.common.core.core.protocol.request.PageDTO;
import com.eip.common.core.core.protocol.response.PageResult;
import com.eip.common.core.core.exception.BusinessRuntimeException;
import com.eip.common.core.service.snowflake.LocalGlobalUidService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseService<RequestDTO, ResponseDTO, Entity, DAO extends BaseDAO<Entity>> {

    @Autowired
    protected LocalGlobalUidService uidService;

    @Autowired
    protected DAO dao;

    protected abstract Entity instanciateEntity();

    protected abstract ResponseDTO instanciateDTO();

    public Long create(RequestDTO createDTO, String defaultStatus) {
        Entity entity = instanciateEntity();
        BeanUtils.copyProperties(createDTO, entity);
        Long id = uidService.nextId();
        setId(entity, id);
        setStatus(entity, defaultStatus);
        log.debug("[base-mapper] insert - {} ", entity.toString());
        dao.insert(entity);
        return id;
    }

    public ResponseDTO show(Long id) {
        BusinessResponseEnum.PARAMS_ID_EMPTY_ERROR.assertNotNull(id);
        ResponseDTO dto = instanciateDTO();
        Entity entity = dao.selectById(id);
        log.debug("[base-mapper] result - {}", entity.toString());
        BusinessResponseEnum.RESULT_DATA_ERROR.assertNotNull(entity);
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public List<ResponseDTO> showByIds(List<Long> ids) {
        BusinessResponseEnum.PARAMS_ID_EMPTY_ERROR.assertNotEmpty(ids);
        List<Entity> entityList = dao.selectBatchIds(ids);
        BusinessResponseEnum.RESULT_DATA_ERROR.assertNotEmpty(ids);
        return convertToDTOList(entityList);
    }

    protected List<ResponseDTO> convertToDTOList(List<Entity> entities) {
        List<ResponseDTO> dtoList = new ArrayList<>();
        entities.forEach(entity -> {
            ResponseDTO dto = instanciateDTO();
            BeanUtils.copyProperties(entity, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    public void modify(Long id, RequestDTO createDTO) {
        BusinessResponseEnum.PARAMS_ID_EMPTY_ERROR.assertNotNull(id);
        BusinessResponseEnum.PARAMS_DATA_EMPTY_ERROR.assertNotNull(createDTO);
        Entity entity = instanciateEntity();
        setId(entity, id);
        BeanUtils.copyProperties(createDTO, entity);
        log.debug("[base-mapper] update - {} ", entity.toString());
        long modified = dao.update(entity);
        BusinessResponseEnum.RESULT_DATA_ERROR.assertEquals(modified, 1);
    }

    public void modifyStatus(Long id, String status) {
        BusinessResponseEnum.PARAMS_ID_EMPTY_ERROR.assertNotNull(id);
        Entity entity = instanciateEntity();
        setId(entity, id);
        setStatus(entity, status);
        log.debug("[base-mapper] update status - {} ", entity.toString());
        long modified = dao.update(entity);
        BusinessResponseEnum.RESULT_DATA_ERROR.assertEquals(modified, 1);
    }

    public void delete(Long id) {
        BusinessResponseEnum.PARAMS_ID_EMPTY_ERROR.assertNotNull(id);
        long modified = dao.deleteById(id);
        BusinessResponseEnum.RESULT_DATA_ERROR.assertEquals(modified, 1);
    }

    protected abstract PageResult<ResponseDTO> pageDTOList(PageDTO page);

    private void setId(Entity entity, long id) {
        try {
            Class klass = entity.getClass();
            Method setId = klass.getMethod("setId", long.class);
            setId.invoke(entity, id);
        } catch (NoSuchMethodException e) {
            throw new BusinessRuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new BusinessRuntimeException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

    private void setStatus(Entity entity, String status) {
        try {
            Class klass = entity.getClass();
            Method setStatus = klass.getMethod("setStatus", String.class);
            setStatus.invoke(entity, status);
        } catch (NoSuchMethodException e) {
            throw new BusinessRuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new BusinessRuntimeException(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new BusinessRuntimeException(e.getMessage());
        }
    }

}
