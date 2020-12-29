package com.ncse.zhhygis.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ncse.zhhygis.entity.CarIntoAreaalarmInfos;
import com.ncse.zhhygis.entity.DateUtil;
import com.ncse.zhhygis.service.CarIntoAreaalarmInfosService;
import com.ncse.zhhygis.utils.ServerResponse;
import com.zh.bean.login.MyStaff;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:  [CarIntoAreaalarmInfosController]
 * Description:  [超时报警]
 * Date:  2018/12/20 15 15
 *
 * @author Xugn
 * @version 1.0.0
 */
@RestController
/**自动返回的是json格式数据***/
@RequestMapping("/carIntoAreaalarmInfos")
@CrossOrigin(allowCredentials = "true")
public class CarIntoAreaalarmInfosController {

    @Autowired
    private CarIntoAreaalarmInfosService carIntoAreaalarmInfosService;

    private final Logger log = Logger.getLogger(this.getClass());
    /***
     * MethodName:  [selectByParms]
     * Description:  [根据条件查询超时报警信息]
     * @param: [ carnum 车牌号, createtimeb 进如时间, createtimee 离开时间, isalarm 是否在线 , driver 司机姓名,regname 区域名称]
     * @return: com.ncse.zhhygis.utils.ServerResponse
     */
    @RequestMapping("selectByParms")
    public ServerResponse selectByParms(MyStaff staff, @RequestParam Map parmsMap){
        //时间戳转换成日期格式,解决前端传入时间格式不正确
        String createtimeb = (String) parmsMap.get("createtimeb");
        String createtimee = (String) parmsMap.get("createtimee");
        if (!StringUtils.isEmpty(createtimeb) && !StringUtils.isEmpty(createtimee)){
            String date1 = DateUtil.timeStamp2Date(createtimeb, "yyyy-MM-dd HH:mm:ss");
            String date2 = DateUtil.timeStamp2Date(createtimee, "yyyy-MM-dd HH:mm:ss");
            parmsMap.put("createtimeb",date1);
            parmsMap.put("createtimee",date2);
        }
        Map map = new HashMap();
        try{
            log.info("selectByParms查询条件："+parmsMap);
            if (parmsMap.get("pageNum") != null && parmsMap.get("pageSize")!=null){
                //进行分页
                PageHelper.startPage(Integer.parseInt((String) parmsMap.get("pageNum")),Integer.parseInt((String) parmsMap.get("pageSize")));
                map.put("pageSize",parmsMap.get("pageSize"));
                map.put("pageNum",parmsMap.get("pageNum"));
            }
            List<CarIntoAreaalarmInfos> carIntoAreaalarmInfos = carIntoAreaalarmInfosService.selectByParms(parmsMap);
            PageInfo<CarIntoAreaalarmInfos> info = new PageInfo<>(carIntoAreaalarmInfos);
            map.put("count",info.getTotal());//获取数据总条数
            map.put("data",carIntoAreaalarmInfos);
            map.put("code","0");
        }catch (Exception e){
            map.put("code","1");
            log.error(e);
        }
        return ServerResponse.createBySuccess(map);
    }
}
