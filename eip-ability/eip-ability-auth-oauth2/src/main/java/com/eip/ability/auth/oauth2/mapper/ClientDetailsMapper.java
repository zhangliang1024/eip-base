package com.eip.ability.auth.oauth2.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eip.ability.auth.oauth2.domain.OAuthClientDetails;
import org.springframework.stereotype.Repository;

/**
 * @author Levin
 */
@Repository
public interface ClientDetailsMapper extends BaseMapper<OAuthClientDetails> {


}
