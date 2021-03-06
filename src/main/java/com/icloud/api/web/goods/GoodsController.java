package com.icloud.api.web.goods;

import com.icloud.annotation.AuthIgnore;
import com.icloud.annotation.LoginUser;
import com.icloud.api.sevice.goods.GoodsBizService;
import com.icloud.api.sevice.groupshop.GroupShopBizService;
import com.icloud.basecommon.model.ApiResponse;
import com.icloud.basecommon.util.SessionUtils;
import com.icloud.basecommon.web.AppBaseController;
import com.icloud.exceptions.ApiException;
import com.icloud.modules.lm.dto.UserDTO;
import com.icloud.modules.lm.dto.goods.SpuDTO;
import com.icloud.modules.lm.model.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/api/goods")
public class GoodsController extends AppBaseController {

   @Autowired
    private GoodsBizService goodsBizService;

    @Autowired
    private GroupShopBizService groupShopBizService;

    @RequestMapping("/getGoodsPage")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title) throws ApiException {
        //获取商户id
        Long supplierId = SessionUtils.getSupplierId(request);
        pageNo = pageNo==null?1:pageNo;
        pageSize = pageSize==null?15:pageSize;
        Map map = getMap("pageNo", pageNo,null);
        getMap("pageSize", pageSize,map);
        getMap("categoryId", categoryId,map);
        getMap("orderBy", orderBy,map);
        getMap("isAsc", isAsc,map);
        getMap("title", title,map);
        getMap("supplierId", supplierId,map);
        println(map,"getGoodsPage");

        Page<SpuDTO> page = goodsBizService.getGoodsPage(pageNo, pageSize, categoryId, orderBy, isAsc, title,supplierId);
        return new ApiResponse().ok(page);
    }

    @RequestMapping("/getGoods")
    @ResponseBody
    @AuthIgnore
    public ApiResponse getGoods(Long spuId, Long groupShopId, @LoginUser UserDTO user) throws ApiException {
        Map map = getMap("spuId", spuId,null);
        getMap("groupShopId", groupShopId,map);
        getMap("userId", user!=null?user.getId():null,map);
        println(map,"getGoods");
        //若团购Id不为空，则额外添加团购信息
        SpuDTO goods = goodsBizService.getGoods(spuId, user!=null?user.getId():null);
        if (groupShopId != null) {
            goods.setGroupShop(groupShopBizService.getGroupShopById(groupShopId));
        }
        return new ApiResponse().ok(goods);
    }


}
