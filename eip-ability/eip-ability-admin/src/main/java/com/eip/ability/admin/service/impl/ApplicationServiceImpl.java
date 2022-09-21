package com.eip.ability.admin.service.impl;

import com.eip.ability.admin.domain.entity.baseinfo.OAuthClientDetails;
import com.eip.ability.admin.mapper.OAuthClientDetailsMapper;
import com.eip.ability.admin.service.ApplicationService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Levin
 */
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl extends SuperServiceImpl<OAuthClientDetailsMapper, OAuthClientDetails> implements ApplicationService {


}
