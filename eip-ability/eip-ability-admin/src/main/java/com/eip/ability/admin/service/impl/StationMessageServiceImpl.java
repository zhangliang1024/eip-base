package com.eip.ability.admin.service.impl;

import com.eip.ability.admin.domain.entity.message.StationMessage;
import com.eip.ability.admin.mapper.StationMessageMapper;
import com.eip.ability.admin.service.StationMessageService;
import com.eip.ability.admin.mybatis.supers.SuperServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Levin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StationMessageServiceImpl extends SuperServiceImpl<StationMessageMapper, StationMessage> implements StationMessageService {


}
