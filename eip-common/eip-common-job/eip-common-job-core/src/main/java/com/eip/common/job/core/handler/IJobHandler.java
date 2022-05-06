package com.eip.common.job.core.handler;

/**
 * job advice
 *
 * @author xuxueli 2015-12-19 19:06:38
 */
public abstract class IJobHandler {


    /**
     * execute advice, invoked when executor receives a scheduling request
     *
     * @throws Exception
     */
    public abstract void execute() throws Exception;


	/*@Deprecated
	public abstract ReturnT<String> execute(String param) throws Exception;*/

    /**
     * init advice, invoked when JobThread init
     */
    public void init() throws Exception {
        // do something
    }


    /**
     * destroy advice, invoked when JobThread destroy
     */
    public void destroy() throws Exception {
        // do something
    }


}
