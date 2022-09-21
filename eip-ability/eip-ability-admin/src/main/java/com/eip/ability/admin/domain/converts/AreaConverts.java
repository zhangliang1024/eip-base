package com.eip.ability.admin.domain.converts;

import com.eip.ability.admin.domain.dto.AreaEntityDTO;
import com.eip.ability.admin.domain.entity.common.AreaEntity;
import com.eip.ability.admin.domain.vo.AreaNodeResp;
import org.springframework.beans.BeanUtils;

/**
 * @author Levin
 */
public class AreaConverts {

    public static final AreaConverts.AreaDto2PoConverts AREA_DTO_2_PO_CONVERTS = new AreaConverts.AreaDto2PoConverts();
    public static final AreaConverts.AreaEntity2NodeRespConverts AREA_ENTITY_2_NODE_RESP_CONVERTS = new AreaConverts.AreaEntity2NodeRespConverts();


    public static class AreaDto2PoConverts implements BaseConverts<AreaEntityDTO, AreaEntity> {
        @Override
        public AreaEntity convert(AreaEntityDTO source) {
            if (source == null) {
                return null;
            }
            AreaEntity target = new AreaEntity();
            BeanUtils.copyProperties(source, target);
            target.setId(source.getId());
            return target;
        }
    }


    public static class AreaEntity2NodeRespConverts implements BaseConverts<AreaEntity, AreaNodeResp> {
        @Override
        public AreaNodeResp convert(AreaEntity source) {
            if (source == null) {
                return null;
            }
            return AreaNodeResp.builder().value(source.getId()).label(source.getName()).parentId(source.getParentId())
                    .level(source.getLevel()).latitude(source.getLatitude()).longitude(source.getLongitude())
                    .isLeaf(source.getLevel() != null && source.getLevel() > 2).build();
        }
    }

}
