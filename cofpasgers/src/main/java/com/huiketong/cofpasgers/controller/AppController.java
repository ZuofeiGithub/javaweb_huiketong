package com.huiketong.cofpasgers.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.aliyuncs.exceptions.ClientException;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.huiketong.cofpasgers.alipay.config.AlipayProperties;
import com.huiketong.cofpasgers.constant.*;
import com.huiketong.cofpasgers.entity.*;
import com.huiketong.cofpasgers.getui.AppPush;
import com.huiketong.cofpasgers.jgim.JPUserService;
import com.huiketong.cofpasgers.json.data.*;
import com.huiketong.cofpasgers.json.response.BaseJsonResponse;
import com.huiketong.cofpasgers.repository.*;
import com.huiketong.cofpasgers.service.IDemand;
import com.huiketong.cofpasgers.util.*;
import com.huiketong.cofpasgers.weixinpay.config.MyWXPayConfig;
import com.huiketong.cofpasgers.weixinpay.util.WxPayApiConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/api")
public class AppController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    UserRightsRepository userRightsRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    EnterpriseRepository enterpriseRepository;
    @Autowired
    SmscodeRepository smscodeRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BannerRepository bannerRepository;
    @Autowired
    BannerContextRepository contextRepository;
    @Autowired
    MerchantsRepository merchantsRepository;
    @Autowired
    CompanyUserRepository companyUserRepository;
    @Autowired
    ShareContextRepository shareContextRepository;
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    DefaultEnterRepository defaultEnterRepository;
    @Autowired
    VersionRepository versionRepository;
    @Autowired
    BankRepository bankRepository;
    @Autowired
    DefaultBankRepository defaultBankRepository;
    @Autowired
    CommissionRepository commissionRepository;
    @Autowired
    EarningsRepository earningsRepository;
    @Autowired
    WithdrawalDetailsRepository withdrawalDetailsRepository;
    @Autowired
    DealFlowRepository dealFlowRepository;
    @Autowired
    RankDataRepository rankDataRepository;
    @Autowired
    CommodityCategoryRepository commodityCategoryRepository;
    @Autowired
    CommodityRepository commodityRepository;
    @Autowired
    CommodityStyleRepository commodityStyleRepository;
    @Autowired
    AttentionGoodsRepository attentionGoodsRepository;
    @Autowired
    CommodityImgRepository commodityImgRepository;
    @Autowired
    CommodityOrderRepository orderRepository;
    @Autowired
    ShareGuideRepository shareGuideRepository;
    @Autowired
    IntegralRuleRepository integralRuleRepository;
    @Autowired
    PointDetailRepository pointDetailRepository;
    @Autowired
    private WXPay wxPay;
    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private InstructionsRepository instructionsRepository;
    @Autowired
    private VoucherUserRepository voucherUserRepository;

    @PostMapping(value = "/get_version")
    @CrossOrigin
    public BaseJsonResponse GetVersion(HttpServletRequest request) {
        String platform = request.getParameter("platform");
        String version = request.getParameter("version");
        BaseJsonResponse response = new BaseJsonResponse();
        if (platform != null) {
            VersionData data = new VersionData();
            Version remote_version = versionRepository.findVersionByPlatform(platform);
            if (remote_version != null) {
                if (version != null && version.equals(remote_version.getVersion())) {
                    data.setDesc(remote_version.getVersion());
                    data.setStatus("0");
                    data.setUrl("");
                    response.setCode("1");
                    response.setMsg("暂无更新");
                    response.setData(data);
                } else {
                    data.setUrl(remote_version.getUrl());
                    data.setStatus(String.valueOf(remote_version.getStatus()));
                    data.setDesc(remote_version.getDescipt());
                    response.setCode("1");
                    response.setData(data);
                }
            } else {
                data.setStatus("0");
                response.setMsg("暂无版本信息");
                response.setCode("1");
                response.setData(data);

            }
        } else {
            response.setCode("200");
            response.setMsg("平台信息不正确");
        }
        return response;
    }

    /**
     * 登陆
     *
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping(value = "login")
    @CrossOrigin
    public BaseJsonResponse Login(HttpServletRequest request) {
        String uname = request.getParameter("uname");
        String pword = request.getParameter("pword");
        String clientid = request.getParameter("clientid");
        String client_token = request.getParameter("client_token");
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(uname)) {

            LoginData data = new LoginData();
            DefaultEnter enter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(uname, uname);
            if (ObjectUtils.isNotNull(enter)) {
                Agent agentUser = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(uname, enter.getCompayId(), uname, enter.getCompayId());
                if (ObjectUtils.isNotEmpty(agentUser.getForbid()) && agentUser.getForbid() == 1) {
                    response.setCode("-2");
                    response.setMsg("用户已被注销");
                } else if (ObjectUtils.isNotNull(agentUser)) {
                    try {
                        if (ObjectUtils.isNotEmpty(pword) && ObjectUtils.isNotEmpty(agentUser.getPassword()) && MD5Util.validPassword(pword, agentUser.getPassword())) {
                            data.setUser_id(agentUser.getLoginUsername());
                            String access_token = TokenUtils.createJwtToken(agentUser.getTelphone());
                            agentRepository.updateAccesstokenAndDeviceId(access_token, clientid, client_token, agentUser.getTelphone());
                            data.setToken(access_token);
                            data.setJg_username("c" + agentUser.getInitCode());
                            data.setInvite_code(agentUser.getInitCode());
                            response.setCode("1");
                            response.setData(data);
                            response.setMsg("登陆成功");
                            int times = pointDetailRepository.findCurLoginPointCount();
                            if (times < 1) {
                                PointDetail pointDetail = new PointDetail();
                                pointDetail.setAddTime(new Date());
                                pointDetail.setCompanyId(agentUser.getCompanyId());
                                pointDetail.setDescript("登陆积分");
                                pointDetail.setStatus(1);
                                pointDetail.setType(PointType.LOGIN.ordinal());
                                pointDetail.setUserId(agentUser.getId());
                                IntegralRule integralRule = integralRuleRepository.findIntegralRuleByCompanyId(agentUser.getCompanyId());
                                if (ObjectUtils.isNotNull(integralRule)) {
                                    pointDetail.setPoint(integralRule.getLoginIntegral());
                                    agentRepository.updatePoint(integralRule.getLoginIntegral(), agentUser.getId());
                                    pointDetailRepository.save(pointDetail);
                                }
                            }


                            //logger.debug("进入logo方法，使用logback打印日志");
                            //mailService.sendSimpleMail("348068347@qq.com","提示","用户"+agentUser.getTelphone()+"登录成功");
                        } else {
                            response.setMsg("密码错误");
                            response.setCode("-1");
                        }
                    } catch (Exception e) {
                        response.setCode("200").setMsg("登陆失败");
                        e.printStackTrace();
                    }
                } else {
                    response.setCode("2");
                    response.setMsg("用户不存在");
                }
            } else {
                response.setCode("-2");
                response.setMsg("用户不存在");
            }
        } else {
            response.setMsg("用户名不能为空");
            response.setCode("100");
        }
        return response;
    }

    /**
     * 首页信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "home")
    @CrossOrigin
    public BaseJsonResponse Home(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        BaseJsonResponse response = new BaseJsonResponse();
        if (user_id != null) {
            List<BannerData> bannerDataList = new ArrayList<>();
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (defaultEnter != null) {
                            List<Banner> bannerList = bannerRepository.findBannersByCompanyIdAndStatusOrderByCreateDate(defaultEnter.getCompayId(), 1);
                            if (bannerList.size() > 0) {
                                for (Banner banner : bannerList) {
                                    BannerData bannerData = new BannerData();
                                    bannerData.setTitle(banner.getDescript());
                                    bannerData.setUrl(banner.getTrankurl());
                                    bannerData.setImage(banner.getImgurl());
                                    bannerDataList.add(bannerData);
                                }
                                response.setData(bannerDataList).setCode("1").setMsg("获取banner");
                            } else {
                                initBanner(bannerDataList);
                            }

                            List<NoticeData> noticeDataList = new ArrayList<>();
                            List<Notice> noticeList = noticeRepository.findAllOrderBy(defaultEnter.getCompayId());
                            if (noticeList.size() > 0) {
                                for (int i = 0; i < noticeList.size(); i++) {
                                    NoticeData noticeData = new NoticeData();
                                    noticeData.setTitle(noticeList.get(i).getContext());
                                    noticeDataList.add(noticeData);
                                }
                            }
                            HomeInfoData homeInfoData = new HomeInfoData();

                            homeInfoData.setCompany(defaultEnter.getCompayName());
                            List<Agent> agentList = agentRepository.findAgentsByCompanyId(defaultEnter.getCompayId());
                            if (defaultEnter.getCompayId() == 28) {
                                homeInfoData.setNum("516");
                            } else if (agentList.size() > 0) {
                                homeInfoData.setNum(String.valueOf(agentList.size()));
                            } else {
                                homeInfoData.setNum("0");
                            }

                            HomeData homeData = new HomeData();
                            List<StoreData> storeDataList = new ArrayList<>();
                            homeData.setNotice(noticeDataList);

                            List<Merchants> merchantsList = merchantsRepository.findMerchantsByEnterId(defaultEnter.getCompayId());
                            if (merchantsList.size() > 0) {
                                for (Merchants merchants : merchantsList) {
                                    StoreData storeData = new StoreData();
                                    storeData.setImage(merchants.getMerLogo());
                                    storeData.setUrl(merchants.getMerUrl());
                                    storeData.setTitle(merchants.getMerName());
                                    storeData.setDesc(merchants.getDescript());
                                    storeDataList.add(storeData);
                                }
                                homeData.setStore(storeDataList);
                            } else {
                                homeData.setStore(storeDataList);
                            }
                            Enterprise enterprise = enterpriseRepository.findEnterpriseById(defaultEnter.getCompayId());
                            if (enterprise != null) {
                                if (enterprise.getBrokerage() != null) {
                                    homeData.setBrokerage(enterprise.getBrokerage().toString());
                                } else {
                                    homeData.setBrokerage("0");
                                }
                            }
                            homeData.setBanner(bannerDataList);
                            homeData.setInfo(homeInfoData);
                            response.setData(homeData);
                            response.setCode("1");
                            response.setMsg("获取首页信息成功");
                        } else {
                            response.setMsg("经纪人没有绑定公司").setCode("2");
                        }
                    } else {
                        response.setCode("500").setMsg("Token过时");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("经纪人不存在");
                }
            } else {
                response.setCode("500");
                response.setMsg("经纪人不存在");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }
        return response;
    }

    private void initBanner(List<BannerData> bannerDataList) {
        for (int i = 0; i < 3; i++) {
            BannerData bannerData = new BannerData();
            if (i == 0) {
                bannerData.setImage("http://image.yzcang.com/hd/banner.png");
                bannerData.setUrl("");
                bannerData.setTitle("");
            } else {
                bannerData.setImage("http://image.yzcang.com/hd/banner" + (i + 1) + ".png");
                bannerData.setUrl("");
                bannerData.setTitle("");
            }
            bannerDataList.add(bannerData);
        }
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "userinfo")
    @CrossOrigin
    public BaseJsonResponse GetUserInfo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        BaseJsonResponse response = new BaseJsonResponse();
        UserData userData = new UserData();
        if (!ObjectUtils.isEmpty(user_id)) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (!ObjectUtils.isEmpty(defaultEnter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (!ObjectUtils.isEmpty(agent)) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        userData.setUser_id(agent.getLoginUsername());
                        userData.setNickname(agent.getAgentName());
                        Enterprise enterprise = enterpriseRepository.findEnterpriseById(agent.getCompanyId());
                        if (!ObjectUtils.isEmpty(enterprise)) {
                            userData.setCompany(enterprise.getEnterName());
                        }
                        userData.setCompany_id(defaultEnter.getCompayId().toString());
                        userData.setInvite_code(agent.getInitCode());
                        Integer points1 = pointDetailRepository.findAllGetPoints(agent.getId(), agent.getCompanyId());
                        Integer points2 = pointDetailRepository.findAllResumPoints(agent.getId(), agent.getCompanyId());
                        if (ObjectUtils.isEmpty(points1)) {
                            points1 = 0;
                        }
                        if (ObjectUtils.isEmpty(points2)) {
                            points2 = 0;
                        }
                        Integer points = 0;
                        if (points1 < points2) {
                            points = 0;
                        } else {
                            points = points1 - points2;
                        }
                        userData.setPoint(points.toString());

                        if (!ObjectUtils.isEmpty(agent.getBCertification())) {
                            userData.setIs_real_name(agent.getBCertification().toString());
                        } else {
                            userData.setIs_real_name("0");
                        }

                        userData.setService_tel(enterprise.getEnterTelphone());
                        userData.setPhone(agent.getTelphone());
                        if (!ObjectUtils.isEmpty(agent.getAvatar())) {
                            userData.setAvatar(Constant.IMAGE_URL + agent.getAvatar());
                        } else {
                            userData.setAvatar("");
                        }

                        if (agent.getAccountBalance() != null) {
                            userData.setAccount(agent.getAccountBalance().toString());
                        } else {
                            userData.setAccount("0.00");
                        }

                        response.setData(userData);
                        response.setCode("1");
                        response.setMsg("获取用户信息成功");
                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }

                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setCode("100");
                response.setMsg("该用户没有与公司绑定");
            }
        } else {
            response.setCode("500");
            response.setMsg("用户不存在");
        }
        return response;
    }

    /**
     * 修改头像
     *
     * @param
     * @param request
     * @return
     */
    @PostMapping(value = "modify_userphone")
    @CrossOrigin
    public BaseJsonResponse ModifyUserPhone(@RequestParam("avatar") MultipartFile file, HttpServletRequest request) throws FileNotFoundException, ParseException {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        BaseJsonResponse response = new BaseJsonResponse();
        String saveDirectory = FileUtil.getUploadDir();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        BaseJsonResponse res = FileUtil.upLoad(file, response, saveDirectory);
                        if (res.getCode() == "0") {
                            UpLoadFileData data = (UpLoadFileData) res.getData();
                            String filename = data.getFilename();
                            agentRepository.updateUserPhoto(filename, agent.getId());
                            UserPhoneData userPhoneData = new UserPhoneData();
                            userPhoneData.setAvatar(Constant.IMAGE_URL + filename);
                            response.setCode("1");
                            response.setData(userPhoneData);
                            response.setMsg("修改头像成功");
                        }
                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setMsg("用户不存在");
                response.setCode("500");
            }
        } else {
            response.setCode("500");
            response.setMsg("用户不存在");
        }

        return response;
    }

    /**
     * 修改昵称
     *
     * @param request
     * @return
     */
    @PostMapping(value = "modify_usernickname")
    @CrossOrigin
    public BaseJsonResponse ModifyUserNickname(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String nickname = request.getParameter("nickname");
        BaseJsonResponse response = new BaseJsonResponse();

        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        agentRepository.updateUserNickName(nickname, agent.getId());
                        response.setCode("1");
                        response.setMsg("修改昵称成功");
                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setMsg("用户不存在");
                response.setCode("500");
            }
        } else {
            response.setCode("500");
            response.setMsg("用户不存在");
        }
        return response;
    }

    /**
     * 发送短信验证码
     *
     * @param request
     * @return
     */
    @PostMapping(value = "send_verificode")
    @CrossOrigin
    public BaseJsonResponse SendVerificode(HttpServletRequest request, HttpSession session) throws ClientException {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String type = request.getParameter("type");
        String phone = request.getParameter("phone");
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(type)) {

            String code = AlicomDysmsUtil.getCode();
            if (type.equals(VerifyCodeType.FINDPWORD)) {
                List<Agent> agentList = agentRepository.findAgentsByTelphoneOrLoginUsername(phone, user_id);
                if (ObjectUtils.isNotNull(agentList) && agentList.size() > 0) {
                    AlicomDysmsUtil.sendSms(phone, code, "SMS_152543000");
                    Smscode smscode = new Smscode();
                    smscode.setCode(code);
                    smscode.setType(type);
                    smscode.setTelphone(phone);
                    smscodeRepository.save(smscode);
                    response.setCode("1").setMsg("发送验证码成功");
                } else {
                    response.setCode("300").setMsg("手机号未注册");
                }
            } else {
                if (ObjectUtils.isNotEmpty(user_id)) {
                    DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
                    if (ObjectUtils.isNotEmpty(defaultEnter)) {
                        Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                                defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                        if (ObjectUtils.isNotNull(agent)) {
                            if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                                if (ObjectUtils.isNotEmpty(type)) {

                                    if (type.equals(VerifyCodeType.WDPWORD)) {
                                        AlicomDysmsUtil.sendSms(agent.getTelphone(), code, "SMS_152543005");
                                        saveVerifyCode(type, agent, code, agent.getTelphone());
                                        response.setCode("1").setMsg("发送验证码成功");
                                    } else if (type.equals(VerifyCodeType.CPPWORD)) {
                                        AlicomDysmsUtil.sendSms(agent.getTelphone(), code, "SMS_152547925");
                                        saveVerifyCode(type, agent, code, agent.getTelphone());
                                        response.setCode("1").setMsg("发送验证码成功");
                                    } else if (type.equals(VerifyCodeType.LOGINEDPWORD)) {
                                        AlicomDysmsUtil.sendSms(agent.getTelphone(), code, "SMS_15242995");
                                        saveVerifyCode(type, agent, code, agent.getTelphone());
                                        response.setCode("1").setMsg("发送验证码成功");
                                    } else {
                                        response.setMsg("无效类型").setCode("1");
                                    }
                                } else {
                                    response.setCode("300").setMsg("信息不完整");
                                }
                            } else {
                                response.setCode("500").setMsg("token过时");
                            }
                        } else {
                            response.setCode("500").setMsg("用户不存在");
                        }
                    } else {
                        response.setCode("500").setMsg("用户没有绑定默认公司");
                    }
                } else {
                    response.setCode("500").setMsg("用户不存在");
                }
            }
        } else {
            response.setCode("300").setMsg("信息不完整");
        }

        return response;
    }

    private void saveVerifyCode(String type, Agent agent, String code, String phone) {
        Smscode smscode = new Smscode();
        smscode.setUserId(agent.getId());
        smscode.setCode(code);
        smscode.setType(type);
        smscode.setTelphone(phone);
        smscodeRepository.save(smscode);
    }

    /**
     * 验证短信验证码
     */
    @PostMapping(value = "verify_code_correct")
    @CrossOrigin
    public BaseJsonResponse VerifyCodeCorrect(HttpServletRequest request) {
        String phone = request.getParameter("phone");
        String verify = request.getParameter("verify");
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(phone) && ObjectUtils.isNotEmpty(verify)) {
            Smscode smscode = smscodeRepository.findSmscodeByTelphoneAndType(phone, VerifyCodeType.FINDPWORD);
            if (ObjectUtils.isNotNull(smscode)) {
                if (ObjectUtils.isNotEmpty(smscode.getCode()) && ObjectUtils.isNotEmpty(verify) && verify.equals(smscode.getCode())) {
                    response.setMsg("验证码正确").setCode("1");
                } else {
                    response.setMsg("验证码错误").setCode("1");
                }
            } else {
                response.setMsg("验证码不存在");
                response.setCode("300");
            }
        } else {
            response.setMsg("信息不完整").setCode("1");
        }
        return response;
    }

    /**
     * 修改提现密码
     *
     * @param request
     * @return
     */
    @PostMapping(value = "modify_safecode")
    @CrossOrigin
    public BaseJsonResponse ModifySafeCode(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String verify = request.getParameter("verify");
        String pword = request.getParameter("pword");
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(user_id)) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (ObjectUtils.isNotNull(defaultEnter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (ObjectUtils.isNotNull(agent)) {
                    if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                        Smscode smscode = smscodeRepository.findSmscodeByTelphoneAndType(agent.getTelphone(), VerifyCodeType.WDPWORD);
                        if (ObjectUtils.isNotNull(smscode)) {
                            if (ObjectUtils.isNotEmpty(verify) && ObjectUtils.isNotEmpty(smscode.getCode()) && verify.equals(smscode.getCode())) {
                                if (ObjectUtils.isNotEmpty(pword)) {
                                    String pwordmd5 = null;
                                    try {
                                        pwordmd5 = MD5Util.getEncryptedPwd(pword);
                                        agentRepository.updateDralPwd(pwordmd5, agent.getId());
                                        response.setCode("1").setMsg("修改提现密码成功");
                                    } catch (Exception e) {
                                        response.setCode("1").setMsg("修改提现密码失败");
                                        e.printStackTrace();
                                    }

                                } else {
                                    response.setMsg("密码为空");
                                    response.setCode("1");
                                }
                            } else {
                                response.setMsg("验证码不正确").setCode("1");
                            }

                        } else {
                            response.setMsg("验证码不存在");
                            response.setCode("1");
                        }
                    } else {
                        response.setCode("500").setMsg("Token过时");
                    }
                } else {
                    response.setCode("500").setMsg("用户不存在");
                }
            } else {
                response.setCode("500").setMsg("用户没有绑定公司");
            }
        } else {
            response.setCode("500").setMsg("用户不存在");
        }
        return response;
    }

    /**
     * 修改登陆密码
     *
     * @param request
     * @return
     */
    @PostMapping(value = "modify_loginpwd")
    @CrossOrigin
    public BaseJsonResponse ModifyLoginPwd(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String verify = request.getParameter("verify");
        String pword = request.getParameter("pword");
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(user_id)) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (ObjectUtils.isNotNull(defaultEnter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (ObjectUtils.isNotNull(agent)) {
                    if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                        Smscode smscode = smscodeRepository.findSmscodeByTelphoneAndType(agent.getTelphone(), VerifyCodeType.LOGINEDPWORD);
                        if (ObjectUtils.isNotNull(smscode)) {
                            if (ObjectUtils.isNotEmpty(verify) && ObjectUtils.isNotEmpty(smscode.getCode()) && verify.equals(smscode.getCode())) {
                                if (ObjectUtils.isNotEmpty(pword)) {
                                    try {
                                        String md5pword = MD5Util.getEncryptedPwd(pword);
                                        agentRepository.modifyPwd(md5pword, agent.getTelphone(), agent.getLoginUsername());
                                        response.setCode("1").setMsg("修改登陆密码成功");
                                    } catch (Exception e) {
                                        response.setCode("2").setMsg("修改登陆密码失败");
                                        e.printStackTrace();
                                    }
                                } else {
                                    response.setMsg("密码为空");
                                    response.setCode("1");
                                }
                            } else {
                                response.setMsg("验证码不正确").setCode("1");
                            }

                        } else {
                            response.setMsg("验证码不存在");
                        }
                    } else {
                        response.setCode("500").setMsg("Token过时");
                    }
                } else {
                    response.setCode("500").setMsg("用户不存在");
                }
            } else {
                response.setCode("500").setMsg("用户没有绑定公司");
            }
        } else {
            response.setCode("500").setMsg("用户不存在");
        }
        return response;
    }

    /**
     * @param user_id
     * @param token
     * @return
     * @throws ParseException
     */
    @PostMapping(value = "signin")
    @CrossOrigin
    public BaseJsonResponse Signin(String user_id, String token) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            Integer count = pointDetailRepository.findCurSignPoint(agent.getId(), agent.getCompanyId());

            if (count >= 1) {
                response.setMsg("您已经签到").setCode("2");
            } else {
                PointDetail pointDetail = new PointDetail();
                pointDetail.setAddTime(new Date());
                pointDetail.setCompanyId(agent.getCompanyId());
                pointDetail.setDescript("签到积分");
                pointDetail.setStatus(1);
                pointDetail.setType(PointType.SIGN.ordinal());
                pointDetail.setUserId(agent.getId());
                IntegralRule integralRule = integralRuleRepository.findIntegralRuleByCompanyId(agent.getCompanyId());
                if (ObjectUtils.isNotNull(integralRule)) {
                    pointDetail.setPoint(integralRule.getSignIntegral());
                    try {
                        pointDetailRepository.save(pointDetail);
                        response.setCode("1").setMsg("签到成功");
                    } catch (Exception e) {
                        response.setCode("200").setMsg("签到失败");
                    }
                }
            }
        });
        return response;
    }

    /**
     * 切换用户
     *
     * @param request
     * @return
     */
    @PostMapping(value = "changeuser")
    @CrossOrigin
    public BaseJsonResponse ChangeUser(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String company_id = request.getParameter("company_id");
        BaseJsonResponse response = new BaseJsonResponse();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (company_id != null) {
                            Enterprise enterprise = enterpriseRepository.findEnterpriseById(Integer.parseInt(company_id));
                            if (enterprise != null) {
                                defaultEnterRepository.updateCompanyid(enterprise.getId(), enterprise.getEnterName(), user_id);
                                response.setCode("1");
                                response.setMsg("切换公司成功");
                            } else {
                                response.setMsg("公司不存在");
                                response.setCode("100");
                            }
                        } else {
                            response.setMsg("公司不存在");
                            response.setCode("100");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("1");
                    response.setMsg("用户不存在");
                }
            }
        } else {
            response.setCode("1");
            response.setMsg("用户不存在");
        }
        return response;
    }

    /**
     * 收益明细
     *
     * @param request
     * @return
     */
    @PostMapping(value = "earndetail")
    @CrossOrigin
    public BaseJsonResponse GetEarnDetail(HttpServletRequest request) throws ParseException {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        BaseJsonResponse response = new BaseJsonResponse();
        List<EarndetailData> earndetailDataList = new ArrayList<>();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (page != null && limit != null) {
                            int limit1 = (Integer.parseInt(page) - 1) * (Integer.parseInt(limit));
                            List<Earnings> earningsList = earningsRepository.FindEarningsByAgentId(agent.getId(), limit1, Integer.parseInt(limit));
                            if (earningsList.size() > 0) {
                                for (Earnings earnings : earningsList) {
                                    EarndetailData earndetailData = new EarndetailData();
                                    earndetailData.setDate(DateUtils.dateFormat(earnings.getAddTime(), DateUtils.DATE_TIME_PATTERN));
                                    earndetailData.setMoney(String.valueOf(earnings.getMoney()));
                                    earndetailData.setDesc(earnings.getDescript());
                                    earndetailDataList.add(earndetailData);
                                }
                                response.setData(earndetailDataList);
                                response.setCode("1");
                                response.setMsg("获取收益明细成功");
                            } else {
                                response.setData(new ArrayList<>());
                                response.setCode("1");
                                response.setMsg("获取收益明细成功");
                            }
                        } else {
                            response.setMsg("信息不完整");
                            response.setCode("100");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("1");
                    response.setMsg("用户不存在");
                }
            }
        } else {
            response.setCode("1");
            response.setMsg("用户不存在");
        }
        return response;
    }

    @PostMapping(value = "commission_withdrawal")
    @CrossOrigin
    public BaseJsonResponse WidthDrawal(HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        BaseJsonResponse response = new BaseJsonResponse();
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String money = request.getParameter("money");
        String pword = request.getParameter("pword");

        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (ObjectUtils.isNotNull(defaultEnter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (ObjectUtils.isNotNull(agent)) {
                    if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                        if (ObjectUtils.isNotEmpty(money) && ObjectUtils.isNotEmpty(pword)) {
                            if (agent.getDrawlPad() != null && MD5Util.validPassword(pword, agent.getDrawlPad())) {
                                if (agent.getAccountBalance() != null && new BigDecimal(money).compareTo(agent.getAccountBalance()) != 1) {
                                    BigDecimal decimal = new BigDecimal(money);
                                    BigDecimal setScale = decimal.setScale(2, BigDecimal.ROUND_HALF_DOWN);
                                    agentRepository.updateProfit(setScale, agent.getId(), agent.getCompanyId());
                                    WithdrawalDetails withdrawalDetails = new WithdrawalDetails();
                                    withdrawalDetails.setMoney(new BigDecimal(money));
                                    withdrawalDetails.setUser_id(agent.getId());
                                    withdrawalDetails.setDrawalTime(new Date());
                                    withdrawalDetails.setDescript("佣金提现");
                                    withdrawalDetails.setComid(agent.getCompanyId());
                                    withdrawalDetails.setStatus(1);
                                    withdrawalDetails.setAngentname(agent.getAgentName());
                                    Bank bank = bankRepository.findFirstByUserId(agent.getId());
                                    if (ObjectUtils.isNotNull(bank)) {
                                        withdrawalDetails.setBankName(bank.getName());
                                        withdrawalDetails.setBankNum(bank.getBankNum());
                                        withdrawalDetailsRepository.save(withdrawalDetails);
                                        response.setCode("1");
                                        response.setMsg("提现成功");
                                    } else {
                                        response.setCode("2").setMsg("用户没有绑定银行卡");
                                    }

                                } else {
                                    response.setCode("300");
                                    response.setMsg("余额不足");
                                }
                            } else {
                                response.setMsg("密码错误");
                                response.setCode("111");
                            }
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("1");
                    response.setMsg("用户不存在");
                }
            }
        } else {
            response.setCode("1");
            response.setMsg("用户不存在");
        }
        return response;
    }

    /**
     * 提现明细
     *
     * @param request
     * @return
     */
    @PostMapping(value = "drawaldetail")
    @CrossOrigin
    public BaseJsonResponse GetDrawalDetail(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        BaseJsonResponse response = new BaseJsonResponse();
        List<DrawalDetailData> dataList = new ArrayList<>();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (page != null && limit != null) {
                            int limit1 = (Integer.parseInt(page) - 1) * (Integer.parseInt(limit));
                            if (limit1 < 0) {
                                limit1 = 0;
                            }
                            List<WithdrawalDetails> withdrawalDetailsList = withdrawalDetailsRepository.findWidthdrawalByAgentId(agent.getId(), limit1, Integer.parseInt(limit));
                            if (withdrawalDetailsList.size() > 0) {
                                for (WithdrawalDetails withdrawalDetails : withdrawalDetailsList) {
                                    DrawalDetailData data = new DrawalDetailData();
                                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                                    String formatStr = formatter.format(withdrawalDetails.getDrawalTime());
                                    data.setDate(formatStr);
                                    data.setMoney(String.valueOf(withdrawalDetails.getMoney()));
                                    data.setDesc(withdrawalDetails.getDescript());
                                    dataList.add(data);
                                }
                                response.setData(dataList);
                                response.setCode("1");
                                response.setMsg("获取提现明细成功");
                            } else {
                                response.setData(new ArrayList<>());
                                response.setCode("1");
                                response.setMsg("获取提现明细成功");
                            }
                        } else {
                            response.setMsg("信息不完整");
                            response.setCode("100");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("1");
                    response.setMsg("用户不存在");
                }
            }
        } else {
            response.setCode("1");
            response.setMsg("用户不存在");
        }
        return response;
    }

    /**
     * 修改手机号码
     *
     * @param request
     * @return
     */
    @PostMapping(value = "modifytelphone")
    @CrossOrigin
    public BaseJsonResponse ModifyTelPhone(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String phone = request.getParameter("phone");
        String verify = request.getParameter("verify");
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(user_id)) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (ObjectUtils.isNotNull(defaultEnter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (ObjectUtils.isNotNull(agent)) {
                    if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                        Smscode smscode = smscodeRepository.findSmscodeByTelphoneAndType(agent.getTelphone(), VerifyCodeType.CPPWORD);
                        if (ObjectUtils.isNotNull(smscode) && ObjectUtils.isNotEmpty(verify) && verify.equals(smscode.getCode())) {
                            if (ObjectUtils.isNotEmpty(phone)) {
                                try {
                                    agentRepository.changeUserPhone(phone, agent.getLoginUsername(), agent.getTelphone());
                                    defaultEnterRepository.updateUserTelphone(phone, agent.getLoginUsername());
                                    response.setCode("1").setMsg("修改手机号码成功");
                                } catch (Exception e) {
                                    response.setCode("300").setMsg("修改手机号码失败");
                                }

                            }
                        } else {
                            response.setMsg("验证码不存在").setCode("300");
                        }
                    } else {
                        response.setCode("500").setMsg("token过时");
                    }
                } else {
                    response.setCode("500").setMsg("用户不存在");
                }
            } else {
                response.setMsg("用户没有绑定公司").setCode("300");
            }
        } else {
            response.setCode("500").setMsg("用户不存在");
        }
        response.setCode("1");
        response.setMsg("修改手机号成功");
        return response;
    }

    @PostMapping(value = "forgetpwd")
    @CrossOrigin
    public BaseJsonResponse ForgetPwd(HttpServletRequest request) {
        String phone = request.getParameter("phone");
        String verify = request.getParameter("verify");
        String pword = request.getParameter("pword");
        BaseJsonResponse response = new BaseJsonResponse();

        if (ObjectUtils.isNotEmpty(phone) && ObjectUtils.isNotEmpty(verify) && ObjectUtils.isNotEmpty(pword)) {
            Smscode smscode = smscodeRepository.findSmscodeByTelphoneAndType(phone, VerifyCodeType.FINDPWORD);
            if (ObjectUtils.isNotNull(smscode) && ObjectUtils.isNotEmpty(verify) && verify.equals(smscode.getCode())) {
                try {
                    String pwordmd5 = MD5Util.getEncryptedPwd(pword);
                    agentRepository.forgetPwd(pwordmd5, phone, phone);
                    response.setMsg("密码修改成功").setCode("1");
                } catch (Exception e) {
                    response.setMsg("密码修改失败").setCode("2");
                    e.printStackTrace();
                }

            } else {
                response.setCode("300").setMsg("验证码不存在");
            }
        } else {
            response.setMsg("信息不完整").setCode("300");
        }
        return response;
    }

    /**
     * 获取用户所在公司
     *
     * @param request
     * @return
     */
    @PostMapping(value = "getusercompany")
    @CrossOrigin
    public BaseJsonResponse GetUserCompany(HttpServletRequest request) {
        BaseJsonResponse response = valideUser(request);
        return response;
    }

    private BaseJsonResponse valideUser(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");

        if (user_id != null && !user_id.isEmpty()) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        GetCompanyList(response, user_id);
                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setCode("200");
                response.setMsg("没有默认公司");
            }
        } else {
            response.setMsg("用户不存在");
        }
        return response;
    }

    private void GetCompanyList(BaseJsonResponse response, String user_id) {
        List<Agent> agentList = agentRepository.findAgentsByTelphoneOrLoginUsername(user_id, user_id);
        if (agentList.size() > 0) {
            List<UserCompanyData> dataList = new ArrayList<>();
            for (Agent agent1 : agentList) {
                UserCompanyData data = new UserCompanyData();
                data.setId(String.valueOf(agent1.getCompanyId()));
                Enterprise enterprise = enterpriseRepository.findEnterpriseById(agent1.getCompanyId());
                data.setName(enterprise.getEnterName());
                dataList.add(data);
            }
            response.setData(dataList);
            response.setCode("1");
            response.setMsg("获取公司列表成功");
        } else {
            response.setCode("300");
            response.setMsg("获取公司列表失败");
        }
    }

    @PostMapping(value = "get_real_name_info")
    @CrossOrigin
    public BaseJsonResponse GetCertifiInfo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        CertificationInfoData data = new CertificationInfoData();
        BaseJsonResponse response = new BaseJsonResponse();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        data.setId_card(agent.getIdCard());
                        data.setId_card_photo(Constant.IMAGE_URL + agent.getCardPhoto());
                        data.setReal_name(agent.getRealName());
                        if (agent.getBCertification() == 3) {
                            data.setReason(agent.getReason());
                        }
                        response.setMsg("获取实名认证信息");
                        response.setCode("1");
                        response.setData(data);
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("用户不存在");
                }
            } else {
                response.setMsg("用户没有绑定公司");
                response.setCode("500");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }

        return response;
    }

    @PostMapping(value = "get_point_detail")
    @CrossOrigin
    public BaseJsonResponse get_point_detail(String user_id, String token, Integer page, Integer limit, String type) throws ParseException, AlipayApiException {

        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            List<PointDetailRespData> dataList = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(limit) && ObjectUtils.isNotEmpty(type)) {
                List<PointDetail> pointDetailList = new ArrayList<>();
                if (type.equals("income")) {
                    pointDetailList = pointDetailRepository.findDetailPoints(agent.getId(), agent.getCompanyId(), 1, (page - 1) * limit, limit);
                } else if (type.equals("expend")) {
                    pointDetailList = pointDetailRepository.findDetailPoints(agent.getId(), agent.getCompanyId(), 2, (page - 1) * limit, limit);
                } else {
                    pointDetailList = pointDetailRepository.findDetailPoints(agent.getId(), agent.getCompanyId(), 0, (page - 1) * limit, limit);
                }

                if (pointDetailList.size() > 0) {
                    for (PointDetail pointDetail : pointDetailList) {
                        PointDetailRespData data = new PointDetailRespData();
                        data.setDesc(pointDetail.getDescript());
                        data.setPoint(pointDetail.getPoint().toString());
                        data.setStatus(pointDetail.getStatus().toString());
                        data.setTime(DateUtils.dateFormat(pointDetail.getAddTime(), DateUtils.DATE_TIME_PATTERN));
                        dataList.add(data);
                    }
                    response.setMsg("获取详情").setCode("1").setData(dataList);
                } else {
                    response.setMsg("获取详情").setCode("1").setData(new ArrayList<>());
                }
            } else {
                response.setMsg("信息不完整").setCode("2");
            }
        });
        return response;
    }

    @PostMapping(value = "get_point_rule")
    @CrossOrigin
    public BaseJsonResponse get_point_rule(String user_id, String token) throws ParseException, AlipayApiException {
        PointRuleData data = new PointRuleData();
        TaskData taskData = new TaskData();
        List<PointDialsData> pointDialsDataList = new ArrayList<>();
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            IntegralRule rule = integralRuleRepository.findIntegralRuleByCompanyId(agent.getCompanyId());
            if (ObjectUtils.isNotNull(rule)) {
                JSONArray jsonArray = JSONArray.fromObject(rule.getDials());
                for (int i = 0; i < jsonArray.size(); i++) {
                    PointDialsData pointDialsData = new PointDialsData();
                    int integral = jsonArray.getJSONObject(i).getInt("integral");
                    int id = jsonArray.getJSONObject(i).getInt("id");
                    pointDialsData.setId(String.valueOf(id));
                    pointDialsData.setPoint(String.valueOf(integral));
                    pointDialsDataList.add(pointDialsData);
                }
                Integer curdaypoint = pointDetailRepository.findCurGetPoint(agent.getId(), agent.getCompanyId());
                if (ObjectUtils.isNotEmpty(curdaypoint)) {
                    data.setDay_point(curdaypoint.toString());
                } else {
                    data.setDay_point("0");
                }
                if (ObjectUtils.isNotEmpty(rule.getConsume())) {
                    data.setConsume(rule.getConsume().toString());
                } else {
                    data.setConsume("0");
                }
                if (ObjectUtils.isNotEmpty(rule.getIsOpenLotto())) {
                    data.setIs_open_lotto(rule.getIsOpenLotto().toString());
                }
                if (ObjectUtils.isNotEmpty(rule.getIsPoint())) {
                    data.setIs_point(rule.getIsPoint().toString());
                }
                data.setDials(pointDialsDataList);
                Integer point1 = pointDetailRepository.findAllGetPoints(agent.getId(), agent.getCompanyId());
                Integer point2 = pointDetailRepository.findAllResumPoints(agent.getId(), agent.getCompanyId());
                if (!ObjectUtils.isNotEmpty(point1)) {
                    point1 = 0;
                }
                if (!ObjectUtils.isNotEmpty(point2)) {
                    point2 = 0;
                }
                Integer point = point1 - point2;
                if (point > 0) {
                    data.setPoint(point.toString());
                } else {
                    data.setPoint(point.toString());
                }
                data.setRmb_for_point(rule.getRmbForPoint().toString());
                data.setMin_money(rule.getMinPrice().toString());
//                Integer signpoints = pointDetailRepository.findAllPoints(PointType.SIGN.ordinal(),agent.getId(),agent.getCompanyId());
//                Integer recompoints = pointDetailRepository.findAllPoints(PointType.RECOM.ordinal(),agent.getId(),agent.getCompanyId());
//                Integer loginpoints = pointDetailRepository.findAllPoints(PointType.LOGIN.ordinal(),agent.getId(),agent.getCompanyId());
//                Integer invitepoints = pointDetailRepository.findAllPoints(PointType.INVITE.ordinal(),agent.getId(),agent.getCompanyId());
//                Integer authpoints = pointDetailRepository.findAllPoints(PointType.AUTH.ordinal(),agent.getId(),agent.getCompanyId());
                if (ObjectUtils.isNotEmpty(rule.getSignIntegral())) {
                    taskData.setSign(rule.getSignIntegral().toString());
                } else {
                    taskData.setSign("0");
                }

                if (ObjectUtils.isNotEmpty(rule.getRecomIntegral())) {
                    taskData.setRecom(rule.getRecomIntegral().toString());
                } else {
                    taskData.setRecom("0");
                }
                if (ObjectUtils.isNotEmpty(rule.getLoginIntegral())) {
                    taskData.setLogin(rule.getLoginIntegral().toString());
                } else {
                    taskData.setLogin("0");
                }

                if (ObjectUtils.isNotEmpty(rule.getInviteIntegral())) {
                    taskData.setInvite(rule.getInviteIntegral().toString());
                } else {
                    taskData.setInvite("0");
                }
                if (ObjectUtils.isNotEmpty(rule.getAuthIntegral())) {
                    taskData.setAuth(rule.getAuthIntegral().toString());
                } else {
                    taskData.setAuth("0");
                }
                data.setTask(taskData);
                data.setDials(pointDialsDataList);
                response.setCode("1").setMsg("获取成功").setData(data);
            } else {
                response.setCode("2").setMsg("未设置积分规则");
            }
        });
        return response;
    }

    @PostMapping(value = "get_account_info")
    @CrossOrigin
    public BaseJsonResponse getAccountInfo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        BaseJsonResponse response = new BaseJsonResponse();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        DefaultBank defaultBank = defaultBankRepository.findDefaultBankByUserId(agent.getId());
                        AccountData data = new AccountData();
                        BankInfoData bankInfoData = new BankInfoData();
                        if (defaultBank != null) {
                            Bank bank = bankRepository.findBankByBankNum(defaultBank.getBankNum());
                            if (bank != null) {
                                bankInfoData.setBank_id(String.valueOf(bank.getBankId()));
                                bankInfoData.setCard_num(bank.getBankNum());
                                data.setBank_info(bankInfoData);
                            } else {
                                data.setBank_info(null);
                            }
                        } else {
                            data.setBank_info(null);
                        }
                        data.setAccount(String.valueOf(agent.getAccountBalance()));
                        Commission commission = commissionRepository.findCommissionByCompanyId(defaultEnter.getCompayId());
                        if (commission != null) {
                            data.setIs_withdraw_open(String.valueOf(commission.getBWithdrawOpen()));
                            data.setMin_withdraw(String.valueOf(commission.getMinWithdraw()));
                        } else {
                            data.setMin_withdraw("100000");
                            data.setIs_withdraw_open("0");
                        }
                        data.setIs_real_name(String.valueOf(agent.getBCertification()));
                        if (agent.getAccountBalance() != null) {
                            data.setAccount(agent.getAccountBalance().toString());
                        } else {
                            data.setAccount("0.00");
                        }

                        response.setData(data);
                        response.setMsg("获取账户信息成功");
                        response.setCode("1");

                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setCode("500");
                response.setMsg("用户没有与该公司绑定");
            }
        } else {
            response.setCode("500");
            response.setMsg("用户不能为空");
        }
        return response;
    }

    @PostMapping(value = "certification")
    @CrossOrigin
    public BaseJsonResponse Certificate(@RequestParam("id_card_photo") MultipartFile file, HttpServletRequest request) throws FileNotFoundException, ParseException {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String real_name = request.getParameter("real_name");
        String id_card = request.getParameter("id_card");
        String saveDirectory = FileUtil.getUploadDir();
        BaseJsonResponse response = new BaseJsonResponse();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token.equals(agent.getAccessToken())) {
                        BaseJsonResponse res = FileUtil.upLoad(file, response, saveDirectory);
                        if (res.getCode() == "0") {
                            UpLoadFileData data = (UpLoadFileData) res.getData();
                            String filename = data.getFilename();
                            agentRepository.certiAgent(filename, id_card, real_name, user_id, agent.getCompanyId(), user_id, agent.getCompanyId());
                            agentRepository.updateCertfication(2, agent.getInitCode());
                            response.setCode("1");
                            response.setMsg("用户认证成功");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("token验证错误");
                    }
                } else {
                    response.setCode("100");
                    response.setMsg("用户不存在");
                }
            } else {
                response.setCode("200");
                response.setMsg("该用户没有与公司绑定");
            }
        } else {
            response.setMsg("该用户不存在");
            response.setCode("500");
        }
        return response;
    }

    @PostMapping(value = "ranklist")
    @CrossOrigin
    public BaseJsonResponse GetRankList(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String type = request.getParameter("type");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        BaseJsonResponse response = new BaseJsonResponse();
        List<RankData> dataList = new ArrayList<>();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (page != null && limit != null) {
                            int limit1 = (Integer.parseInt(page) - 1) * (Integer.parseInt(limit));
                            if (limit1 < 0) {
                                response.setCode("111");
                                response.setMsg("信息错误");
                            } else {
                                String users = agentRepository.findUserIds(agent.getCompanyId());
                                List<Integer> userids = new ArrayList<>();
                                String[] strusers = users.split(",");
                                for (String str : strusers) {
                                    userids.add(Integer.parseInt(str));
                                }
                                if (type != null && type.equals("all")) {
                                    List<RankData> agentList = rankDataRepository.findAllRanks(userids, limit1, Integer.parseInt(limit));
                                    GetRankList(dataList, agentList);
                                    response.setMsg("获取总排行版");
                                    response.setCode("1");
                                    response.setData(dataList);
                                } else if (type != null && type.equals("week")) {
                                    List<RankData> agentList = rankDataRepository.findWeekRanks(userids, limit1, Integer.parseInt(limit));
                                    GetRankList(dataList, agentList);
                                    response.setMsg("获取周排行版");
                                    response.setCode("1");
                                    response.setData(dataList);
                                } else {
                                    response.setMsg("排行类型不正确");
                                    response.setCode("111");
                                }
                            }

                        } else {
                            response.setMsg("信息不完整");
                            response.setCode("111");
                        }

                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setMsg("用户没有绑定公司");
                response.setCode("100");
            }
        } else {
            response.setCode("500");
            response.setMsg("用户不存在");
        }
        return response;
    }

    private void GetRankList(List<RankData> dataList, List<RankData> agentList) {
        if (agentList != null) {
            for (RankData agent1 : agentList) {
                RankData data = new RankData();
                data.setNickname(agent1.getNickname());
                data.setClient_num(agent1.getClient_num());
                data.setMoney(agent1.getMoney());
                dataList.add(data);
            }
        }
    }

    @PostMapping(value = "get_guide")
    @CrossOrigin
    public BaseJsonResponse GetGuide(String user_id, String token) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            ShareFeeGuide shareFeeGuide = shareGuideRepository.findShareFeeGuideByCompanyId(agent.getCompanyId());
            if (ObjectUtils.isNotEmpty(shareFeeGuide)) {
                if (!shareFeeGuide.getGuideContext().isEmpty()) {
                    response.setData(shareFeeGuide.getGuideContext()).setMsg("获取规则成功").setCode("1");
                } else {
                    response.setData("暂无规则").setMsg("获取规则成功").setCode("1");
                }
            } else {
                response.setData("").setCode("1").setMsg("获取失败");
            }
        });
        return response;
    }

    @PostMapping(value = "my_invite_agent")
    @CrossOrigin
    public BaseJsonResponse GetMyInviteAgent(HttpServletRequest request, Integer page, Integer limit) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
//        String page = request.getParameter("page");
//        String limit = request.getParameter("limit");
        BaseJsonResponse response = new BaseJsonResponse();
        List<InviteAgentData> dataList = new ArrayList<>();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        List<Agent> agentList = agentRepository.findPageNextAgents(agent.getId(), agent.getCompanyId(), limit * (page - 1), limit);
                        if (agentList.size() > 0) {
                            try {
                                for (Agent agent1 : agentList) {
                                    InviteAgentData data = new InviteAgentData();
                                    data.setName(agent1.getAgentName());
                                    data.setPhone(agent1.getTelphone());
                                    if (ObjectUtils.isNotEmpty(agent1.getInitAgentNam())) {
                                        data.setTj_num(String.valueOf(agent1.getReconCustomNam()));
                                    } else {
                                        data.setTj_num("0");
                                    }
                                    if (ObjectUtils.isNotEmpty(agent1.getReconCustomNam())) {
                                        data.setYq_num(String.valueOf(agent1.getInitAgentNam()));
                                    } else {
                                        data.setYq_num("0");
                                    }
                                    if (ObjectUtils.isNotEmpty(agent1.getDealCustomNum())) {
                                        data.setCj_num(String.valueOf(agent1.getDealCustomNum()));
                                    } else {
                                        data.setCj_num("0");
                                    }
                                    data.setAdd_time(DateUtils.dateFormat(agent1.getRegDatetime(), DateUtils.DATE_TIME_PATTERN));
                                    dataList.add(data);
                                }
                            } catch (Exception e) {
                                response.setMsg("获取失败");
                                response.setCode("300");
                                e.printStackTrace();
                            }
                            response.setData(dataList).setCode("1").setMsg("获取成功");

                        } else {
                            response.setCode("1");
                            response.setMsg("获取成功").setData(dataList);
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("用户不存在");
                }
            } else {
                response.setMsg("用户没有绑定公司");
                response.setCode("500");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }

        return response;
    }

    @PostMapping(value = "ranklistdetail")
    @CrossOrigin
    public BaseJsonResponse GetRankListDetail(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        RankListDetailData data = new RankListDetailData();
        data.setInfo("3213");
        data.setNickname("李四");
        response.setData(data);
        return response;
    }

    private BaseJsonResponse verifyCustomer(String context, BaseJsonResponse response, String code, String msg) {
        if (context == null && context.isEmpty()) {
            response.setMsg("名字不能为空");
            response.setCode("100");
        } else {
            return response;
        }
        return response;
    }

    /**
     * 报备客户
     *
     * @param request
     * @return
     */
    @PostMapping(value = "customer")
    @CrossOrigin
    public BaseJsonResponse Customer(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String style = request.getParameter("style");
        String scheme = request.getParameter("scheme");
        String area = request.getParameter("area");
        String budget = request.getParameter("budget");
        String remark = request.getParameter("remark");
        //String company = request.getParameter("company");
        if (user_id != null) {

        }
        DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
        if (defaultEnter != null) {
            Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
            if (agent != null) {
                if (token != null && token.equals(agent.getAccessToken())) {
                    if (name != null && phone != null && address != null && style != null && scheme != null && area != null && budget != null && remark != null) {
                        Customer customer = new Customer();
                        customer.setAgentId(agent.getId());
                        customer.setCustomerName(name);
                        customer.setTelphone(phone);
                        customer.setAgentName(agent.getAgentName());
                        customer.setDetailAddress(address);
                        customer.setStyle(Integer.parseInt(style));
                        customer.setScheme(Integer.parseInt(scheme));
                        customer.setHouseArea(Integer.parseInt(area));
                        customer.setRenovBudget(Integer.parseInt(budget));
                        customer.setRenovRemark(remark);
                        customer.setCompanyId(defaultEnter.getCompayId());
                        customer.setRecomDatetime(new Date());
                        customer.setSignPrice(new BigDecimal(0));
                        customer.setVerifyStatus(1);
                        agentRepository.updateCustomeNum(agent.getId(), defaultEnter.getCompayId());
                        customerRepository.save(customer);
                        //agentRepository.updateScore();
                        Agent cagent = agentRepository.findIsCusAgents(agent.getCompanyId());
                        if (ObjectUtils.isNotNull(cagent)) {
                            AppPush.pushToSigle(cagent.getDeviceId(), "客户报备", agent.getAgentName() + "报备了新客户");
                        }


                        response.setCode("1");
                        response.setMsg("报备客户成功");
                    } else {
                        response.setCode("200");
                        response.setMsg("信息不完整");
                    }

                } else {
                    response.setCode("500");
                    response.setMsg("用户Token过期");
                }

            } else {
                response.setCode("100");
                response.setMsg("用户不存在");
            }
        }
        return response;
    }

    @PostMapping(value = "businesslist")
    @CrossOrigin
    public BaseJsonResponse GetBusinessList(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(),
                        defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        List<Merchants> merchantsList = merchantsRepository.findMerchantsByEnterId(defaultEnter.getCompayId());
                        List<BusinessListData> dataList = new ArrayList<>();
                        if (merchantsList.size() > 0) {
                            for (Merchants merchants : merchantsList) {
                                BusinessListData data = new BusinessListData();
                                data.setImage(merchants.getMerLogo());
                                data.setUrl(merchants.getMerUrl());
                                data.setTitle(merchants.getMerName());
                                dataList.add(data);
                            }
                            response.setData(dataList);
                            response.setCode("1");
                            response.setMsg("获取商家列表成功");
                        } else {
                            response.setCode("1");
                            response.setMsg("获取商家列表成功");
                            response.setData(dataList);
                        }

                    } else {
                        response.setCode("500");
                        response.setMsg("权限超时");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("-1");
                }
            } else {
                response.setMsg("用户没有绑定公司");
                response.setCode("400");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }

        return response;
    }

    /**
     * 分享赚佣金列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "sharecommissionlist")
    @CrossOrigin
    public BaseJsonResponse ShareComm(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        BaseJsonResponse response = new BaseJsonResponse();
        List<ShareCommiData> dataList = new ArrayList<>();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (page != null && limit != null) {
                            int real_limit = Integer.parseInt(limit) * (Integer.parseInt(page) - 1);
                            if (real_limit < 0) {
                                response.setCode("-200");
                                response.setMsg("页数不能小于0");
                            } else {
                                List<ShareContext> shareContextList = shareContextRepository.findPagesByLimit(defaultEnter.getCompayId(), real_limit, Integer.parseInt(page));
                                if (shareContextList.size() > 0) {
                                    for (ShareContext context : shareContextList) {
                                        ShareCommiData data = new ShareCommiData();
                                        data.setId(String.valueOf(context.getId()));
                                        data.setImage(context.getImgUrl());
                                        data.setPrice(context.getMoney().toString());
                                        data.setTitle(context.getTitle());
                                        dataList.add(data);
                                    }
                                    response.setMsg("获取分享列表成功");
                                    response.setCode("1");
                                    response.setData(dataList);
                                } else {
                                    response.setMsg("获取分享列表成功");
                                    response.setCode("1");
                                    response.setData(new ArrayList<>());
                                }
                            }
                        } else {
                            response.setMsg("错误");
                            response.setCode("-200");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token超时");
                    }
                }
            }
        } else {
            response.setData("500");
            response.setMsg("用户不存在");
        }
        return response;
    }

    /**
     * 分享赚佣金详情列表
     *
     * @return
     */
    @PostMapping(value = "sharedetaillist")
    @CrossOrigin
    public BaseJsonResponse GetShareDetailList(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String id = request.getParameter("id");
        BaseJsonResponse response = new BaseJsonResponse();
        ShareComDetailData data = new ShareComDetailData();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (id != null) {
                            ShareContext context = shareContextRepository.findShareContextById(Integer.parseInt(id));
                            if (context != null) {
                                data.setContent(context.getContext());
                                data.setTitle(context.getTitle());
                                data.setPrice(context.getMoney().toString());
                                data.setUrl(Constant.URL + "invite/customer?parentInvitationCode=" + agent.getInitCode() + "&id=" + context.getId());
                                data.setImage(context.getImgUrl());
                                response.setData(data);
                                response.setCode("1");
                                response.setMsg("获取列表成功");
                            } else {
                                response.setData("");
                                response.setCode("1");
                                response.setMsg("获取列表成功");
                            }
                        } else {
                            response.setMsg("数据不正确");
                            response.setCode("300");
                        }

                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setCode("200");
                response.setMsg("用户没有与公司绑定");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }
        return response;
    }

    @PostMapping(value = "share_success")
    @CrossOrigin
    /**
     * 分享赚佣金成功
     */
    public BaseJsonResponse ShareSuccess(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String id = request.getParameter("id");
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (id != null) {
                            ShareContext context = shareContextRepository.findShareContextById(Integer.parseInt(id));
                            if (context != null) {
                                Commission commission = commissionRepository.findCommissionByCompanyId(agent.getCompanyId());
                                if (ObjectUtils.isNotNull(commission)) {
                                    int times = earningsRepository.shareTimes(agent.getId(), 1);
                                    if (ObjectUtils.isNotNull(times) && ObjectUtils.isNotEmpty(commission.getShareCount()) && (times >= commission.getShareCount())) {
                                        response.setCode("2");
                                        response.setMsg("今日分享次数已达上限");
                                    } else {
                                        agentRepository.updateScore(context.getMoney(), agent.getId(), agent.getCompanyId());
                                        agentRepository.updateAccount(context.getMoney(), agent.getId(), agent.getCompanyId());
                                        Earnings earnings = new Earnings();
                                        earnings.setUserId(agent.getId());
                                        earnings.setAngentname(agent.getAgentName());
                                        earnings.setCusname("");
                                        earnings.setComId(agent.getCompanyId());
                                        earnings.setMoney(context.getMoney());
                                        earnings.setDescript("分享佣金发放");
                                        earnings.setAddTime(new Date());
                                        earnings.setType(1);
                                        earningsRepository.save(earnings);
                                        response.setCode("1");
                                        response.setMsg("分享成功");
                                    }
                                }
                            } else {
                                response.setCode("300");
                                response.setMsg("分享信息不正确");
                            }
                        } else {
                            response.setMsg("信息不正确");
                            response.setCode("300");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("用户不存在");
                }
            } else {
                response.setMsg("用户没有与公司绑定");
                response.setCode("300");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }
        return response;
    }

    /**
     * 获取银行卡列表
     */
    @PostMapping(value = "get_bank_info")
    @CrossOrigin
    public BaseJsonResponse GetBankInfo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        BaseJsonResponse response = new BaseJsonResponse();
        List<BankCardData> dataList = new ArrayList<>();

        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        DefaultBank defaultBank = defaultBankRepository.findDefaultBankByUserId(agent.getId());
                        List<Bank> bankList = bankRepository.findBanksByUserId(agent.getId());
                        if (bankList.size() > 0) {
                            for (Bank bank : bankList) {
                                BankCardData data = new BankCardData();
                                data.setId(String.valueOf(bank.getId()));
                                data.setBank_id(String.valueOf(bank.getBankId()));
                                if (bank.getBankNum().equals(defaultBank.getBankNum())) {
                                    data.setBank_default("1");
                                } else {
                                    data.setBank_default("0");
                                }
                                data.setBank_num(String.valueOf(bank.getBankNum()));
                                dataList.add(data);
                            }
                            response.setData(dataList);
                            response.setCode("1");
                            response.setMsg("获取银行卡列表成功");
                        } else {
                            response.setCode("1");
                            response.setMsg("获取银行卡列表成功");
                            response.setData(dataList);
                        }
                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setCode("500");
                response.setMsg("用户没有绑定公司");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }
        return response;
    }

    @PostMapping(value = "set_bank_info")
    @CrossOrigin
    public BaseJsonResponse SetBankInfo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String bank_id = request.getParameter("id");
        BaseJsonResponse response = new BaseJsonResponse();

        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(),
                        defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        List<Bank> bankList = bankRepository.findBanksByUserId(agent.getId());
                        if (bankList.size() > 0) {
                            for (int i = 0; i < bankList.size(); i++) {
                                if (bankList.get(i).getId() == Integer.parseInt(bank_id)) {
                                    defaultBankRepository.updateDefaultBank(bankList.get(i).getBankNum(), agent.getId());
                                    response.setMsg("设置默认银行卡成功");
                                    response.setCode("1");
                                }
                            }
                        } else {
                            response.setCode("0");
                            response.setMsg("您没有银行卡");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("用户不存在");
                }
            }
        }
        return response;
    }

    @PostMapping(value = "add_bank_info")
    @CrossOrigin
    public BaseJsonResponse AddBankInfo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String name = request.getParameter("name");//持卡人姓名
        String bank_id = request.getParameter("bank_id");//哪个银行卡
        String city = request.getParameter("city");//省市
        String branch = request.getParameter("branch");//支行
        String bank_num = request.getParameter("bank_num");//银行卡号
        BaseJsonResponse response = new BaseJsonResponse();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        if (name != null && bank_id != null && city != null && branch != null && bank_num != null) {
                            DefaultBank exist_default_userid = defaultBankRepository.findDefaultBankByUserId(agent.getId());
                            if (exist_default_userid != null) {
                                if (bank_num.equals(exist_default_userid.getBankNum())) {
                                    defaultBankRepository.updateDefaultBank(bank_num, exist_default_userid.getUserId());
                                }
                            } else {
                                DefaultBank defaultBank = new DefaultBank();
                                defaultBank.setBankNum(bank_num);
                                defaultBank.setUserId(agent.getId());
                                defaultBankRepository.save(defaultBank);
                            }
                            Bank existBank = bankRepository.findBankByBankNum(bank_num);
                            if (existBank == null) {
                                Bank bank = new Bank();
                                bank.setName(name);
                                bank.setUserId(agent.getId());
                                bank.setBankId(Integer.parseInt(bank_id));
                                bank.setCity(city);
                                bank.setBranch(branch);
                                bank.setBankNum(bank_num);
                                bankRepository.save(bank);
                                response.setMsg("银行卡添加成功");
                                response.setCode("1");
                            } else {
                                response.setCode("120");
                                response.setMsg("银行卡已经存在");
                            }

                        } else {
                            response.setMsg("信息不完整");
                            response.setCode("300");
                        }
                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("用户不存在");
                }
            } else {
                response.setCode("500");
                response.setMsg("用户没有绑定公司");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }
        return response;
    }

    @PostMapping(value = "del_bank")
    @CrossOrigin
    public BaseJsonResponse DelBank(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String id = request.getParameter("id");
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        Bank bank = bankRepository.findBankById(Integer.parseInt(id));
                        if (bank != null) {
                            DefaultBank defaultBank = defaultBankRepository.findDefaultBankByBankNum(bank.getBankNum());

                            if (defaultBank != null) {
                                bankRepository.deleteById(Integer.parseInt(id));
                                defaultBankRepository.delete(defaultBank);
                                List<Bank> bankList = bankRepository.findBanksByUserId(agent.getId());
                                if (bankList.size() > 0) {
                                    DefaultBank defaultBank1 = new DefaultBank();
                                    defaultBank1.setUserId(bankList.get(0).getUserId());
                                    defaultBank1.setBankNum(bankList.get(0).getBankNum());
                                    defaultBankRepository.save(defaultBank1);
                                }
                                response.setMsg("删除银行卡成功");
                                response.setCode("1");
                            } else {
                                bankRepository.deleteById(Integer.parseInt(id));
                                response.setMsg("删除银行卡成功");
                                response.setCode("1");
                            }

                        } else {
                            response.setMsg("银行卡不存在");
                            response.setCode("200");
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }
                } else {
                    response.setMsg("用户不存在");
                    response.setCode("500");
                }
            } else {
                response.setCode("100");
                response.setMsg("用户没有绑定企业");
            }
        } else {
            response.setMsg("用户不存在");
            response.setCode("500");
        }
        return response;
    }

    @PostMapping(value = "get_mine_customer")
    @CrossOrigin
    public BaseJsonResponse GetMineCustomer(String user_id, String token, Integer page, Integer limit, String status) throws ParseException {
        BaseJsonResponse response = new BaseJsonResponse();
        if (ObjectUtils.isNotEmpty(user_id)) {
            DefaultEnter enter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (ObjectUtils.isNotEmpty(enter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, enter.getCompayId(), user_id, enter.getCompayId());
                if (ObjectUtils.isNotEmpty(agent) && ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                    if (page != null && limit != null && status != null) {
                        Integer limit1 = limit * (page - 1);
                        if (limit1 < 0) {
                            response.setMsg("页数不能小于0");
                            response.setCode("300");
                        } else {
                            List<Customer> customerList = new ArrayList<>();
                            if (status.equals("0")) {
                                customerList = customerRepository.findAllCustomers(agent.getId(), limit1, limit);
                            } else {
                                customerList = customerRepository.findPageCustomers(Integer.parseInt(status), agent.getId(), limit1, limit);
                            }

                            List<MCustomerData> data = new ArrayList<>();
                            if (customerList.size() > 0) {
                                for (Customer customer : customerList) {
                                    MCustomerData customerData = new MCustomerData();
                                    customerData.setName(customer.getCustomerName());
                                    customerData.setAddress(customer.getDetailAddress());
                                    customerData.setTel(customer.getTelphone());
                                    customerData.setRemark(customer.getRenovRemark());
                                    customerData.setSex(customer.getSex());
                                    customerData.setAdd_time(DateUtils.dateFormat(customer.getRecomDatetime(), DateUtils.DATE_TIME_PATTERN));
                                    if (customer.getSignPrice() != null) {
                                        customerData.setSign_price(customer.getSignPrice().toString());
                                    } else {
                                        customerData.setSign_price("0.00");
                                    }
                                    if (customer.getRevokeReason() != null) {
                                        customerData.setReject(customer.getRevokeReason());
                                    } else {
                                        customerData.setReject("无");
                                    }
                                    if (ObjectUtils.isNotEmpty(customer.getVerifyStatus())) {
                                        customerData.setStatus(CustomerStatus.STATUS(customer.getVerifyStatus()));
                                    } else {
                                        customerData.setStatus("1");
                                    }
                                    data.add(customerData);
                                }
                                response.setMsg("获取客户列表");
                                response.setCode("1");
                                response.setData(data);

                            } else {
                                response.setMsg("没有客户信息");
                                response.setCode("1");
                                response.setData(new ArrayList<>());
                            }
                        }

                    } else {
                        response.setMsg("请求参数不完整");
                        response.setCode("110");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("Token超时");
                }
            } else {
                response.setCode("300").setMsg("用户没有绑定默认公司");
            }
        } else {
            response.setMsg("用户不存在").setCode("500");
        }
        return response;
    }

    @PostMapping(value = "get_nextline_customer")
    @CrossOrigin
    public BaseJsonResponse GetNextlineCustomer(HttpServletRequest request) throws ParseException {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        String status = request.getParameter("status");
        BaseJsonResponse response = new BaseJsonResponse();
        List<NextLineCustomerData> dataList = new ArrayList<>();

        if (user_id != null) {
            DefaultEnter enter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (enter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, enter.getCompayId(), user_id, enter.getCompayId());
                if (ObjectUtils.isNotNull(agent)) {
                    if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                        List<Agent> nextAgents = agentRepository.findNextAgents(agent.getId(), agent.getCompanyId());
                        if (nextAgents.size() > 0) {
                            for (Agent agent1 : nextAgents) {
                                if (ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(limit) && ObjectUtils.isNotEmpty(status)) {
                                    Integer limit1 = Integer.parseInt(limit) * (Integer.parseInt(page) - 1);
                                    if (limit1 < 0) {
                                        response.setMsg("页数不能小于0");
                                        response.setCode("300");
                                    } else {
                                        List<Customer> customerList = new ArrayList<>();
                                        if (status.equals("0")) {
                                            customerList = customerRepository.findAllCustomers(agent1.getId(), limit1, Integer.parseInt(limit));
                                        } else {
                                            customerList = customerRepository.findPageCustomers(Integer.parseInt(status), agent1.getId(), limit1, Integer.parseInt(limit));
                                        }
                                        if (customerList.size() > 0) {
                                            for (Customer customer : customerList) {
                                                NextLineCustomerData data = new NextLineCustomerData();
                                                data.setEco(agent1.getAgentName());
                                                data.setAddress(customer.getDetailAddress());
                                                data.setName(customer.getCustomerName());
                                                data.setTel(customer.getTelphone());
                                                data.setStyle(DecorationStyle.STYLE(customer.getStyle()));
                                                data.setRemark(customer.getRenovRemark());
                                                data.setAdd_time(DateUtils.dateFormat(customer.getRecomDatetime(), DateUtils.DATE_TIME_PATTERN));
                                                data.setSign_price(customer.getSignPrice().toString());
                                                data.setReject(customer.getRevokeReason());
                                                data.setStatus(customer.getVerifyStatus().toString());
                                                data.setSex(customer.getSex());
                                                dataList.add(data);
                                            }

                                        } else {
                                            response.setMsg("没有客户信息");
                                            response.setCode("1");
                                            response.setData(dataList);
                                        }
                                    }

                                }

                                response.setData(dataList);
                            }

                            response.setCode("1");
                            response.setMsg("下线客户获取成功");
                        } else {
                            response.setMsg("没有下级经纪人").setCode("1").setData(new ArrayList<>());
                        }
                    } else {
                        response.setCode("500");
                        response.setMsg("Token过时");
                    }

                } else {
                    response.setMsg("该用户不存在");
                    response.setCode("300");
                }
            } else {
                response.setCode("300");
                response.setMsg("该用户为与企业绑定");
            }
        } else {
            response.setCode("500").setMsg("用户不存在");
        }
        return response;
    }

    @PostMapping(value = "get_share_invite_info")
    @CrossOrigin
    public BaseJsonResponse GetShareInviteInfo(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        BaseJsonResponse response = new BaseJsonResponse();
        if (user_id != null) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (defaultEnter != null) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (agent != null) {
                    if (token != null && token.equals(agent.getAccessToken())) {
                        CompanyBindUser companyBindUser = companyUserRepository.findCompanyBindUserByInviteCode(agent.getInitCode());
                        if (companyBindUser != null) {

                            ShareInviteData data = new ShareInviteData();
                            data.setCode(companyBindUser.getInviteCode());
                            data.setUrl(Constant.URL + "invite/agent?inviteCode=" + companyBindUser.getInviteCode());
                            data.setTitle("邀请您注册成为经纪人");
                            data.setText(agent.getAgentName() + "邀请你成为经纪人");
                            data.setImage("http://image.yzcang.com/static/install/jiketong/image/logo.png");
                            Integer inviteMoneys = earningsRepository.getShareInviteMoneys(agent.getId(), agent.getCompanyId());
                            if (ObjectUtils.isNotEmpty(inviteMoneys)) {
                                data.setInvite_money(inviteMoneys.toString());
                            } else {
                                data.setInvite_money("0");
                            }

                            data.setInvite_num(agent.getInitAgentNam().toString());
                            response.setData(data);
                            response.setCode("1");
                            response.setMsg("获取邀请信息成功");
                        } else {
                            response.setMsg("用户没有绑定公司").setCode("100");
                        }
                    } else {
                        response.setCode("500").setMsg("Token过期");
                    }
                } else {
                    response.setMsg("用户不存在").setCode("500");
                }

            } else {
                response.setCode("100").setMsg("用户没有默认公司");
            }
        } else {
            response.setCode("100").setMsg("用户不存在");
        }
        return response;
    }

    @PostMapping(value = "get_mine_agent")
    @CrossOrigin
    public BaseJsonResponse GetMineAgent(HttpServletRequest request) {
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        String page = request.getParameter("page");
        String limit = request.getParameter("limit");
        BaseJsonResponse response = new BaseJsonResponse();

        InviteAgentData data = new InviteAgentData();
        data.setName("");
        data.setPhone("");
        data.setAdd_time("");
        data.setTj_num("");
        data.setCj_num("");
        data.setYq_num("");

        response.setData(data);
        response.setCode("1");
        response.setMsg("获取我的邀请人信息");
        return response;
    }

    /**
     * 获取特价购分类信息
     *
     * @param user_id
     * @param token
     * @return
     */
    @PostMapping(value = "get_specialoffer_categorize_info")
    @CrossOrigin
    public BaseJsonResponse getSpecialofferInfo(String user_id, String token) throws ParseException, AlipayApiException {
        List<SpecialofferType> typeList = new ArrayList<>();
        List<SpecialofferStyle> styleList = new ArrayList<>();
        SpecialofferData data = new SpecialofferData();
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            getGoodsCategory(typeList, styleList, data, response, (Agent) o);
        });
        return response;
    }

    /**
     * 获取商品分类信息
     *
     * @param typeList
     * @param styleList
     * @param data
     * @param response
     * @param o
     */
    private void getGoodsCategory(List<SpecialofferType> typeList, List<SpecialofferStyle> styleList, SpecialofferData data, BaseJsonResponse response, Agent o) {
        Agent agent = o;
        if (ObjectUtils.isNotNull(agent)) {
            List<CommodityCategory> commodityCategoryList = commodityCategoryRepository.findAllByCompanyId(agent.getCompanyId());
            if (commodityCategoryList.size() > 0) {
                for (CommodityCategory commodityCategory : commodityCategoryList) {
                    SpecialofferType type = new SpecialofferType();
                    type.setName(commodityCategory.getCategoryName());
                    type.setId(commodityCategory.getId().toString());
                    typeList.add(type);
                }
                data.setType(typeList);
            } else {
                data.setType(new ArrayList<>());
            }
            List<CommodityStyle> commodityStyleList = commodityStyleRepository.findAllCommodityStyle(agent.getCompanyId());
            if (commodityStyleList.size() > 0) {
                for (CommodityStyle commodityStyle : commodityStyleList) {
                    SpecialofferStyle style = new SpecialofferStyle();
                    style.setName(commodityStyle.getTypeName());
                    style.setId(commodityStyle.getId().toString());
                    styleList.add(style);
                }
                data.setStyle(styleList);
            } else {
                data.setStyle(new ArrayList<>());
            }
            response.setData(data).setMsg("获取分类信息成功").setCode("1");
        }
    }

    /**
     * 验证用户权限
     *
     * @param user_id
     * @param token
     * @param response
     * @param demand
     */
    private void verifyUser(String user_id, String token, BaseJsonResponse response, IDemand demand) throws ParseException, AlipayApiException {
        if (ObjectUtils.isNotEmpty(user_id)) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (ObjectUtils.isNotNull(defaultEnter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(user_id, defaultEnter.getCompayId(), user_id, defaultEnter.getCompayId());
                if (agent != null) {
                    if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                        demand.bussiness(agent);
                    } else {
                        response.setMsg("Token过期").setCode("500");
                    }
                } else {
                    response.setCode("500").setMsg("用户不存在");
                }

            } else {
                response.setCode("100").setMsg("用户没有默认公司");
            }
        } else {
            response.setMsg("用户不存在").setCode("100");
        }
    }

    @PostMapping(value = "get_specialoffer_product_info")
    @CrossOrigin
    public BaseJsonResponse getSpecialofferProductInfo(String user_id, String token, Integer type, Integer style, Integer sort, Integer page, Integer limit) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            List<SpecialofferProductData> dataList = new ArrayList<>();
            if (ObjectUtils.isNotEmpty(type) && ObjectUtils.isNotEmpty(style) && ObjectUtils.isNotEmpty(sort) && ObjectUtils.isNotEmpty(page) && ObjectUtils.isNotEmpty(limit)) {
                List<Commodity> commodityList = new ArrayList<>();
                if (sort == 1) {
                    commodityList = commodityRepository.findPageCommodityDESC(type, style, agent.getCompanyId(), sort, (page - 1) * limit, limit);
                } else if (sort == 2) {
                    commodityList = commodityRepository.findPageCommodityASC(type, style, agent.getCompanyId(), sort, (page - 1) * limit, limit);
                } else if (sort == 0) {
                    commodityList = commodityRepository.findPageCommodityASC(type, style, agent.getCompanyId(), sort, (page - 1) * limit, limit);
                }
                if (commodityList.size() > 0) {
                    for (Commodity commodity : commodityList) {
                        CommodityImg commodityImg = commodityImgRepository.findFirstByCommodityd(commodity.getId());
                        if (ObjectUtils.isNotNull(commodityImg)) {
                            SpecialofferProductData data = new SpecialofferProductData();
                            data.setId(commodity.getId().toString());
                            data.setConcern(commodity.getConcernedPeople().toString());
                            data.setRaw_price(commodity.getOriginalPrice().toString());
                            data.setSale_price(commodity.getActivityPrice().toString());
                            data.setUnit(commodity.getDanwei());
                            data.setTitle(commodity.getCommodityName());
                            data.setImage(commodityImg.getCommodityImgUrl());
                            data.setScheme_id(type.toString());
                            data.setStyle_id(style.toString());
                            dataList.add(data);
                        }
                    }
                    response.setData(dataList).setMsg("获取商品列表").setCode("1");
                } else {
                    response.setData(new ArrayList<>()).setCode("1").setMsg("商品列表为空");
                }
            } else {
                response.setCode("2").setMsg("信息不完整");
            }

        });
        return response;
    }

    @PostMapping(value = "get_specialer_product_detail_info")
    @CrossOrigin
    public BaseJsonResponse getSpecialofferProductDetailInfo(String user_id, String token, Integer id) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            if (ObjectUtils.isNotEmpty(id)) {
                Commodity commodity = commodityRepository.findCommoditieByIdAndCompanyId(id, agent.getCompanyId());
                if (ObjectUtils.isNotNull(commodity)) {
                    SpecialofferProductDetailData data = new SpecialofferProductDetailData();
                    List<CommodityImg> commodityImgList = commodityImgRepository.findCommodityImgsByCommodityd(id);
                    List<String> images = new ArrayList<>();
                    if (commodityImgList.size() > 0) {

                        for (CommodityImg commodityImg : commodityImgList) {
                            images.add(commodityImg.getCommodityImgUrl());
                        }
                        data.setImage(images);
                        if (ObjectUtils.isNotEmpty(images.get(0))) {
                            data.setLogo(images.get(0));
                        }
                    } else {
                        data.setImage(new ArrayList<>());
                        data.setLogo("");
                    }
                    data.setTitle(commodity.getCommodityName());
                    data.setId(commodity.getId().toString());
                    data.setUnit(commodity.getDanwei());
                    data.setSale_price(commodity.getActivityPrice().toString());
                    data.setRaw_price(commodity.getOriginalPrice().toString());
                    data.setConcern(commodity.getConcernedPeople().toString());
                    data.setActivity(commodity.getActivityDescription());
                    data.setDetail(commodity.getProductDetails());
                    data.setDeposit(commodity.getDepositMoney().toString());
                    AttentionGoods attentionGoods = attentionGoodsRepository.findAttentionGoodsByGoodsIdAndUserId(id, agent.getId());
                    if (ObjectUtils.isNotNull(attentionGoods)) {
                        data.setIs_focus(attentionGoods.getStatus().toString());
                    } else {
                        data.setIs_focus("0");
                    }
                    response.setData(data).setMsg("获取商品详情").setCode("1");
                } else {
                    response.setMsg("商品不存在").setCode("2");
                }
            } else {
                response.setMsg("商品不存在").setCode("2");
            }

        });
        return response;
    }

    @PostMapping(value = "focus_specialer_product")
    @CrossOrigin
    public BaseJsonResponse focusSpecialerProduct(String user_id, String token, Integer id, Integer status) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            focusGoods(id, status, response, (Agent) o);

        });
        return response;
    }

    /**
     * 关注特价购商品
     *
     * @param id
     * @param status
     * @param response
     * @param o
     */
    private void focusGoods(Integer id, Integer status, BaseJsonResponse response, Agent o) {
        Agent agent = o;
        if (ObjectUtils.isNotEmpty(status)) {
            if (ObjectUtils.isNotEmpty(id)) {
                Commodity commodity = commodityRepository.findCommodityById(id);
                if (ObjectUtils.isNotNull(commodity)) {
                    if (status == 0) {
                        try {
                            attentionGoodsRepository.updateStatus(status, id, agent.getId());
                            commodityRepository.reduceConcerned(id);
                            response.setCode("1").setMsg("取消关注成功");
                        } catch (Exception e) {
                            response.setCode("1").setMsg("取消关注失败");
                        }
                    } else if (status == 1) {
                        AttentionGoods existattentiongoods = attentionGoodsRepository.findAttentionGoodsByGoodsIdAndUserId(id, agent.getId());
                        if (ObjectUtils.isNotNull(existattentiongoods)) {
                            try {
                                attentionGoodsRepository.updateStatus(status, id, agent.getId());
                                commodityRepository.addConcerned(id);
                                response.setCode("1").setMsg("关注成功");
                            } catch (Exception e) {
                                response.setCode("1").setMsg("关注失败");
                            }
                        } else {
                            AttentionGoods attentionGoods = new AttentionGoods();
                            attentionGoods.setCompanyId(agent.getCompanyId());
                            attentionGoods.setGoodsId(id);
                            attentionGoods.setUserId(agent.getId());
                            attentionGoods.setStatus(1);
                            try {
                                attentionGoodsRepository.save(attentionGoods);
                                response.setCode("1").setMsg("关注成功");
                            } catch (Exception e) {
                                response.setCode("1").setMsg("关注失败");
                            }
                        }
                    }
                } else {
                    response.setCode("1").setMsg("商品不存在");
                }
            } else {
                response.setCode("1").setMsg("商品号不存在");
            }
        } else {
            response.setCode("1").setMsg("未知状态");
        }
    }

    @PostMapping(value = "pay_specialer_product")
    @CrossOrigin
    public BaseJsonResponse paySpecialerProduct(String user_id, String token, Integer id, String pay_type) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();

        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            if (ObjectUtils.isNotEmpty(id)) {
                Commodity commodity = commodityRepository.findCommodityById(id);
                if (ObjectUtils.isNotNull(commodity)) {

                    BigDecimal money = commodity.getDepositMoney().multiply(new BigDecimal(100)).setScale(0);
                    if (!ObjectUtils.isNotEmpty(money)) {
                        response.setCode("2").setMsg("您输入的金额错误");
                    } else {
                        if (ObjectUtils.isNotEmpty(pay_type) && pay_type.equals("wxpay")) {
                            weixinPay(id, response, agent, commodity, money);
                        } else if (ObjectUtils.isNotEmpty(pay_type) && pay_type.equals("alipay")) {
                            aliPay(id, response, agent, commodity, money);
                        }
                    }
                } else {
                    response.setCode("2").setMsg("产品不存在");
                }
            } else {
                response.setCode("2").setMsg("产品不存在");
            }

        });
        return response;
    }

    private void aliPay(Integer id, BaseJsonResponse response, Agent agent, Commodity commodity, BigDecimal money) throws AlipayApiException {
        OrderInfoData data = new OrderInfoData();
        String ordernumber = OrderUtil.getInstance().getOrderCode() + OrderUtil.getOrderCodePostfix();
        JSONObject reqData = new JSONObject();
        //订单号,必填
        reqData.put("out_trade_no", ordernumber);
        //PC支付 FAST_INSTANT_TRADE_PAY, APP支付 QUICK_MSECURITY_PAY, 移动H5支付 QUICK_WAP_PAY
        reqData.put("product_code", "FAST_INSTANT_TRADE_PAY");
        //付款金额,必填
        reqData.put("total_amount", money.divide(new BigDecimal(100)));
        //订单描述,必填
        reqData.put("subject", "特价购");
        //该笔订单允许的最晚付款时间，逾期将关闭交易
        //data.put("timeout_express","");
        //公共校验参数
        //data.put("passback_params","");
        //PC支付
        //AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //APP支付
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //移动H5支付
        //AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        //异步通知地址
        request.setNotifyUrl(alipayProperties.getNotifyUrl());
        //同步通知地址
        request.setReturnUrl("");
        //业务参数
        request.setBizContent(reqData.toString());
        AlipayTradeAppPayResponse alipayResponse = alipayClient.sdkExecute(request);
        String order_info = alipayResponse.getBody();

        CommodityOrder order = new CommodityOrder();
        order.setCompanyId(agent.getCompanyId());
        order.setUserId(agent.getId());
        order.setActivityPrice(commodity.getActivityPrice());
        order.setOrderNum(ordernumber);
        order.setInsertTime(new Date());
        order.setUserId(agent.getId());
        order.setStatus(0);
        order.setCommodityId(id);
        order.setOrderStatus(0);
        order.setCompanyId(agent.getCompanyId());
        order.setCommodityName(commodity.getCommodityName());
        CommodityImg commodityImg = commodityImgRepository.findFirstByCommodityd(id);
        if (ObjectUtils.isNotNull(commodityImg)) {
            order.setCommodityOrderImg(commodityImg.getCommodityImgUrl());
        } else {
            order.setCommodityOrderImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547526427629&di=2317ed8ee57a95c83ceeb2a31395e435&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20140611%2F20140611145529-1600143101.jpg");
        }

        order.setPayType("支付宝App支付");
        orderRepository.save(order);
        data.setOrderInfo(order_info);
        data.setOrder_num(ordernumber);
        response.setCode("1").setMsg("下单成功").setData(data);
    }

    private void weixinPay(Integer id, BaseJsonResponse response, Agent agent, Commodity commodity, BigDecimal money) {
        OrderInfoData data = new OrderInfoData();
        String ordernumber = OrderUtil.getInstance().getOrderCode() + OrderUtil.getOrderCodePostfix();
        Map<String, String> reqData = new HashMap<>();
        reqData.put("out_trade_no", ordernumber);
        reqData.put("trade_type", "APP");
//                        reqData.put("product_id",  commodity.getId().toString());
        reqData.put("body", "集客通-特价购");
        reqData.put("total_fee", money.toString());
        // APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
        reqData.put("spbill_create_ip", RequestUtil.GetIp());
        // 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
        reqData.put("notify_url", "http://jkt365.com/notify");
        // 自定义参数, 可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
        reqData.put("device_info", "WEB");
        // 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
        reqData.put("attach", "");

        Map<String, String> responseMap = new HashMap<>();
        try {
            responseMap = wxPay.unifiedOrder(reqData);
            logger.error(responseMap.toString());
            String returnCode = responseMap.get("return_code");
            MyWXPayConfig config = new MyWXPayConfig();
            if (WXPayConstants.SUCCESS.equals(returnCode)) {
                String resultCode = responseMap.get("result_code");
                if (WXPayConstants.SUCCESS.equals(resultCode)) {
                    String appid = responseMap.get("appid");
                    String noncestr = responseMap.get("nonce_str");
                    String pack = "Sign=WXPay";
                    String partnerid = responseMap.get("mch_id");
                    String prepayid = responseMap.get("prepay_id");
                    String timestamp = WxPayApiConfig.New().getTimestamp();
                    CommodityOrder order = new CommodityOrder();
                    order.setCompanyId(agent.getCompanyId());
                    order.setUserId(agent.getId());
                    order.setActivityPrice(commodity.getActivityPrice());
                    order.setOrderNum(ordernumber);
                    order.setInsertTime(new Date());
                    order.setUserId(agent.getId());
                    order.setStatus(0);
                    order.setCommodityId(id);
                    order.setOrderStatus(0);
                    order.setCompanyId(agent.getCompanyId());
                    order.setCommodityName(commodity.getCommodityName());
                    CommodityImg commodityImg = commodityImgRepository.findFirstByCommodityd(id);
                    if (ObjectUtils.isNotNull(commodityImg)) {
                        order.setCommodityOrderImg(commodityImg.getCommodityImgUrl());
                    } else {
                        order.setCommodityOrderImg("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1547526427629&di=2317ed8ee57a95c83ceeb2a31395e435&imgtype=0&src=http%3A%2F%2Fpic.baike.soso.com%2Fp%2F20140611%2F20140611145529-1600143101.jpg");
                    }

                    order.setPayType("微信App支付");
                    try {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("appid", appid);
                        map.put("noncestr", noncestr);
                        map.put("package", pack);
                        map.put("partnerid", partnerid);
                        map.put("prepayid", prepayid);
                        map.put("timestamp", timestamp);
                        String sign = config.getSign(map);
                        Map<String, String> respmap = new HashMap<String, String>();
                        respmap.put("appid", appid);
                        respmap.put("noncestr", noncestr);
                        respmap.put("package", pack);
                        respmap.put("partnerid", partnerid);
                        respmap.put("prepayid", prepayid);
                        respmap.put("timestamp", timestamp);
                        respmap.put("sign", sign);
                        data.setOrderInfo(JSONObject.fromObject(respmap).toString());
                        data.setOrder_num(ordernumber);
                        orderRepository.save(order);
                        response.setData(data).setMsg("下单成功").setCode("1");
                    } catch (Exception e) {
                        response.setMsg("下单失败").setCode("2");
                    }
                } else {
                    response.setMsg("下单失败").setCode("2");
                }
            } else {
                response.setMsg("下单失败").setCode("2");
            }
        } catch (Exception e) {
            response.setMsg("下单失败").setCode("3");
        }
    }

    /**
     * 获取订单状态
     *
     * @param user_id
     * @param token
     * @param order_id
     * @return
     */
    @PostMapping("get_order_status")
    @CrossOrigin
    public BaseJsonResponse getOrderStatus(String user_id, String token, String order_id) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            if (ObjectUtils.isNotEmpty(order_id)) {
                CommodityOrder order = orderRepository.findCommodityOrderByOrderNum(order_id);
                if (ObjectUtils.isNotNull(order)) {
                    Integer status = order.getOrderStatus();
                    OrderStatusData data = new OrderStatusData();
                    data.setStatus(status.toString());
                    response.setMsg("获取订单状态").setCode("1").setData(data);
                } else {
                    response.setMsg("订单不存在").setCode("2");
                }
            }
        });
        return response;
    }

    @PostMapping("get_my_order")
    @CrossOrigin
    public BaseJsonResponse getMyOrder(String user_id, String token, Integer page, Integer limit) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;

            List<CommodityOrder> orderList = orderRepository.findOrderPage(agent.getId(), agent.getCompanyId(), (page - 1) * limit, limit);
            if (orderList.size() > 0) {
                List<MyOrderInfoData> dataList = new ArrayList<>();
                for (CommodityOrder order : orderList) {
                    MyOrderInfoData data = new MyOrderInfoData();
                    data.setAdd_time(DateUtils.dateFormat(order.getInsertTime(), DateUtils.DATE_TIME_PATTERN));
                    data.setImage(order.getCommodityOrderImg());
                    data.setPrice(order.getActivityPrice().toString());
                    data.setStatus(order.getStatus().toString());
                    data.setTitle(order.getCommodityName());
                    data.setId(order.getCommodityId().toString());
                    dataList.add(data);
                }
                response.setData(dataList).setCode("1").setMsg("我的订单");
            } else {
                response.setMsg("无订单数据").setCode("1").setData(new ArrayList<>());
            }
        });
        return response;
    }

    /**
     * get_client_info
     * 获取客户信息
     */
    @PostMapping(value = "get_client_info")
    @CrossOrigin
    public BaseJsonResponse getClientInfo(HttpServletRequest request) {
        BaseJsonResponse response = new BaseJsonResponse();
        String user_id = request.getParameter("user_id");
        String token = request.getParameter("token");
        AgentInfoData data = new AgentInfoData();
        if (ObjectUtils.isNotEmpty(user_id)) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserIdOrUserTelphone(user_id, user_id);
            if (ObjectUtils.isNotNull(defaultEnter)) {
                Agent agent = agentRepository.findAgentByTelphoneAndCompanyIdOrLoginUsernameAndCompanyId(defaultEnter.getUserTelphone(), defaultEnter.getCompayId(), defaultEnter.getUserId(), defaultEnter.getCompayId());
                if (ObjectUtils.isNotNull(agent)) {
                    if (ObjectUtils.isNotEmpty(token) && token.equals(agent.getAccessToken())) {
                        List<Banner> bannerList = bannerRepository.findBannersByCompanyIdAndStatusOrderByCreateDate(agent.getCompanyId(), 1);
                        List<BannerData> bannerDataList = new ArrayList<>();
                        if (bannerList.size() > 0) {
                            for (Banner context : bannerList) {
                                BannerData bannerData = new BannerData();
                                bannerData.setImage(context.getImgurl());
                                bannerData.setUrl(context.getTrankurl());
                                bannerData.setTitle(context.getDescript());
                                bannerDataList.add(bannerData);
                            }
                            data.setBanner(bannerDataList);
                        } else {
                            initBanner(bannerDataList);
                        }

//                        if (ObjectUtils.isNotEmpty(agent.getInitAgentNam())) {
//                            data.setMy_report_client(agent.getInitAgentNam().toString());
//                        } else {
//                            data.setMy_report_client("0");
//                        }

//                        List<Agent> nextagentList = agentRepository.findNextAgents(agent.getId(), agent.getCompanyId());
//                        int reportclient = 0;
//                        int inviteagent = 0;
//                        int dealmake = 0;
//                        if (nextagentList.size() > 0) {
//                            for (Agent agent1 : nextagentList) {
//                                if (ObjectUtils.isNotEmpty(agent1.getReconCustomNam())) {
//                                    reportclient += agent1.getReconCustomNam();
//                                } else {
//                                    reportclient = 0;
//                                }
//                                if (ObjectUtils.isNotEmpty(agent1.getInitAgentNam())) {
//                                    inviteagent += agent1.getInitAgentNam();
//                                } else {
//                                    inviteagent = 0;
//                                }
//                                if (ObjectUtils.isNotEmpty(agent1.getDealCustomNum())) {
//                                    dealmake += agent1.getDealCustomNum();
//                                } else {
//                                    dealmake = 0;
//                                }
//
//                            }
//                        } else {
//                            inviteagent = 0;
//                            reportclient = 0;
//                        }
//                        if (ObjectUtils.isNotEmpty(agent.getReconCustomNam())) {
//                            data.setMy_report_client(agent.getReconCustomNam().toString());
//                        } else {
//                            data.setMy_report_client("0");
//                        }

//                        data.setLine_report_client(String.valueOf(reportclient));
//                        if (ObjectUtils.isNotEmpty(agent.getInitAgentNam())) {
//                            data.setMy_invite_broker(agent.getInitAgentNam().toString());
//                        } else {
//                            data.setMy_invite_broker("0");
//                        }
//
//                        data.setLine_invite_broker(String.valueOf(inviteagent));
//                        if (ObjectUtils.isNotEmpty(agent.getDealCustomNum())) {
//                            data.setMy_make(agent.getDealCustomNum().toString());
//                        } else {
//                            data.setMy_make("0");
//                        }
//
//                        data.setLine_make(String.valueOf(dealmake));
                        int makenum = customerRepository.selectCountDealCustomer(agent.getId(), agent.getCompanyId());
                        int waitnum = customerRepository.selectCountWaitCustomer(agent.getId(), agent.getCompanyId());
                        data.setWait_client(String.valueOf(waitnum));
                        data.setMake_client(String.valueOf(makenum));
                        Integer reportNum = customerRepository.findMonthCustomers(agent.getId(), agent.getCompanyId());
                        Integer visitNum = customerRepository.findMonthvisitCustomers(agent.getId(), agent.getCompanyId());
                        Integer mabNum = customerRepository.findMonthmabCustomers(agent.getId(), agent.getCompanyId());
                        data.setReport(reportNum.toString());
                        data.setVisit(visitNum.toString());
                        data.setMab(mabNum.toString());
                        response.setData(data);
                        response.setMsg("获取客户信息成功");
                        response.setCode("1");
                    } else {
                        response.setMsg("Token过时");
                        response.setCode("500");
                    }
                } else {
                    response.setCode("500");
                    response.setMsg("经纪人不存在");
                }
            } else {
                response.setMsg("经纪人没有与公司绑定");
                response.setCode("200");
            }
        } else {
            response.setCode("500");
            response.setMsg("用户不存在");
        }
        return response;
    }

    @PostMapping("reporte_customer")
    @CrossOrigin
    public BaseJsonResponse reporteCustomer(String user_id, String token, String name, String sex, String tel, String address, String remark) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            reporteCustomer(name, sex, tel, address, remark, response, (Agent) o);
        });
        return response;
    }

    /**
     * 报备客户
     *
     * @param name
     * @param sex
     * @param tel
     * @param address
     * @param remark
     * @param response
     * @param o
     */
    private void reporteCustomer(String name, String sex, String tel, String address, String remark, BaseJsonResponse response, Agent o) {
        Agent agent = o;
        if (ObjectUtils.isNotEmpty(name) && ObjectUtils.isNotEmpty(tel) && ObjectUtils.isNotEmpty(sex) && ObjectUtils.isNotEmpty(address)) {
            Customer existcustomer = customerRepository.findCustomerByCustomerNameAndTelphone(name, tel);
            if (ObjectUtils.isNotNull(existcustomer)) {
                response.setMsg("客户已经存在").setCode("2");
            } else {
                Customer customer = new Customer();
                customer.setCustomerName(name);
                customer.setSex(sex);
                customer.setTelphone(tel);
                customer.setDetailAddress(address);
                customer.setRenovRemark(remark);
                customer.setAgentId(agent.getId());
                customer.setVerifyStatus(1);
                customer.setRecomDatetime(new Date());
                //无关属性
                customer.setScheme(0);
                try {
                    agentRepository.updateCustomeNum(agent.getId(), agent.getCompanyId());
                    customer.setCompanyId(agent.getCompanyId());
                    if (ObjectUtils.isNotEmpty(agent.getAgentName())) {
                        customer.setAgentName(agent.getAgentName());
                    }
                    customerRepository.save(customer);
                    Agent cagent = agentRepository.findIsCusAgents(agent.getCompanyId());
                    if (ObjectUtils.isNotNull(cagent)) {
                        JPUserService service = new JPUserService();
                        StringBuffer context = new StringBuffer();
                        if (ObjectUtils.isNotEmpty(remark)) {
                            context.append("经纪人姓名:" + agent.getAgentName() + "\r\n" +
                                    "客户姓名:" + name + "\r\n" +
                                    "性别:" + sex + "\r\n" +
                                    "客户电话:" + tel + "\r\n" +
                                    "楼盘地址:" + address + "\r\n" +
                                    "备注:" + remark);
                        } else {
                            context.append("经纪人姓名:" + agent.getAgentName() +
                                    "客户姓名:" + name + "\r\n" +
                                    "性别:" + sex + "\r\n" +
                                    "客户电话:" + tel + "\r\n" +
                                    "楼盘地址:" + address);
                        }
                        service.sendSingleTextByAdmin("admin", "c" + cagent.getInitCode(), context.toString());
                        AppPush.pushToSigle(cagent.getDeviceId(), "客户报备", agent.getAgentName() + "报备了新客户");
                    }

                    response.setMsg("报备客户成功").setCode("1");
                } catch (Exception e) {
                    response.setCode("2").setMsg("报备客户失败");
                }
            }
        } else {
            response.setMsg("信息不完整").setCode("2");
        }
    }

    @PostMapping("get_chat_info")
    @CrossOrigin
    public BaseJsonResponse getChatInfo(String user_id, String token) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            Enterprise enterprise = enterpriseRepository.findEnterpriseById(agent.getCompanyId());
            if (ObjectUtils.isNotNull(enterprise)) {
                ChatData data = new ChatData();
                List<AgentListData> dataList = new ArrayList<>();
                if (agent.getType() == AgentType.CUSERVICE.ordinal()) {
                    data.setUsername("c" + agent.getInitCode());
                    data.setCompany_username(enterprise.getEnterName());
                    if (ObjectUtils.isNotEmpty(enterprise.getEnterLogo())) {
                        data.setCompany_avatar(Constant.IMAGE_URL + enterprise.getEnterLogo());
                    } else {
                        data.setUser_avatar("");
                    }

                    if (ObjectUtils.isNotEmpty(agent.getAvatar())) {
                        data.setUser_avatar(Constant.IMAGE_URL + agent.getAvatar());
                    } else {
                        data.setUser_avatar("");
                    }
                    List<Agent> agentList = agentRepository.findNotCusAgents(agent.getCompanyId());
                    if (agentList.size() > 0) {
                        for (Agent agent1 : agentList) {
                            AgentListData listData = new AgentListData();
                            if (ObjectUtils.isNotEmpty(agent1.getAvatar())) {
                                listData.setAvatar(Constant.IMAGE_URL + agent1.getAvatar());
                            } else {
                                listData.setAvatar("");
                            }

                            listData.setName(agent1.getAgentName());
                            listData.setUsername("c" + agent1.getInitCode());
                            dataList.add(listData);
                        }
                        data.setUser_list(dataList);
                    }
                } else {
                    data.setUsername("c" + agent.getInitCode());
                    Agent cagent = agentRepository.findIsCusAgents(agent.getCompanyId());
                    if (ObjectUtils.isNotNull(cagent)) {
                        data.setCompany_username("c" + cagent.getInitCode());
                    } else {
                        data.setCompany_username(enterprise.getEnterLoginName());
                    }

                    if (ObjectUtils.isNotEmpty(enterprise.getEnterLogo())) {
                        data.setCompany_avatar(Constant.IMAGE_URL + enterprise.getEnterLogo());
                    } else {
                        data.setUser_avatar("");
                    }

                    if (ObjectUtils.isNotEmpty(agent.getAvatar())) {
                        data.setUser_avatar(Constant.IMAGE_URL + agent.getAvatar());
                    } else {
                        data.setUser_avatar("");
                    }
                }
                response.setCode("1").setMsg("获取聊天信息成功").setData(data);
            } else {
                response.setMsg("企业不存在").setCode("2");
            }
        });
        return response;
    }

    @PostMapping(value = "point_lotto")
    @CrossOrigin
    public BaseJsonResponse luckyDraw(String user_id, String token) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            LottoData data = new LottoData();

            IntegralRule rule = integralRuleRepository.findIntegralRuleByCompanyId(agent.getCompanyId());
            if (ObjectUtils.isNotNull(rule)) {
                if (rule.getIsOpenLotto() != 2) {
                    response.setMsg("未开启抽奖活动").setCode("1");
                } else {
                    JSONArray jsonArray = JSONArray.fromObject(rule.getDials());
                    if (jsonArray.size() > 0) {
                        List<Gift> giftList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            Gift gift = new Gift();
                            gift.setId(jsonArray.getJSONObject(i).getInt("id"));
                            gift.setProb(jsonArray.getJSONObject(i).getInt("probability") / 100D);
                            gift.setPoint(jsonArray.getJSONObject(i).getInt("integral"));
                            giftList.add(gift);
                        }
                        int index = DrawLotteryUtil.drawGift(giftList);
                        data.setId(giftList.get(index).getId().toString());
                        Integer point1 = pointDetailRepository.findAllGetPoints(agent.getId(), agent.getCompanyId());
                        Integer point2 = pointDetailRepository.findAllResumPoints(agent.getId(), agent.getCompanyId());
                        if (!ObjectUtils.isNotEmpty(point1)) {
                            point1 = 0;
                        }
                        if (!ObjectUtils.isNotEmpty(point2)) {
                            point2 = 0;
                        }
                        Integer point = point1 - point2;
                        if (rule.getConsume() > 0 && point >= rule.getConsume()) {
                            PointDetail pointDetail = new PointDetail();
                            pointDetail.setAddTime(new Date());
                            pointDetail.setCompanyId(agent.getCompanyId());
                            pointDetail.setDescript("抽奖消耗");
                            pointDetail.setStatus(2);
                            pointDetail.setType(PointType.LOTTO.ordinal());
                            pointDetail.setUserId(agent.getId());
                            pointDetail.setPoint(rule.getConsume());
                            PointDetail pointDetail1 = new PointDetail();
                            pointDetail1.setAddTime(new Date());
                            pointDetail1.setCompanyId(agent.getCompanyId());
                            pointDetail1.setDescript("抽奖获得");
                            pointDetail1.setStatus(1);
                            pointDetail1.setType(PointType.LOTTO.ordinal());
                            pointDetail1.setUserId(agent.getId());
                            pointDetail1.setPoint(giftList.get(index).getPoint());
                            try {
                                pointDetailRepository.save(pointDetail);
                                pointDetailRepository.save(pointDetail1);
                                Integer point11 = pointDetailRepository.findAllGetPoints(agent.getId(), agent.getCompanyId());
                                Integer point21 = pointDetailRepository.findAllResumPoints(agent.getId(), agent.getCompanyId());
                                if (!ObjectUtils.isNotEmpty(point1)) {
                                    point1 = 0;
                                }
                                if (!ObjectUtils.isNotEmpty(point2)) {
                                    point2 = 0;
                                }
                                Integer point33 = point11 - point21;
                                if (point33 > 0) {
                                    data.setPoint(point33.toString());
                                } else {
                                    data.setPoint("0");
                                }
                                response.setCode("1").setMsg("抽奖成功").setData(data);
                            } catch (Exception e) {
                                response.setCode("2").setMsg("抽奖失败");
                            }
                        } else {
                            response.setMsg("您的积分余额不足").setCode("2");
                        }


                    } else {
                        response.setMsg("未开启抽奖活动").setCode("1");
                    }


                }
            } else {
                response.setMsg("未开启积分抽奖活动").setCode("1");
            }
        });
        return response;
    }

    @PostMapping(value = "redeem")
    @CrossOrigin
    public BaseJsonResponse RedeemPoint(String user_id, String token, String money) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            IntegralRule rule = integralRuleRepository.findIntegralRuleByCompanyId(agent.getCompanyId());
            if (ObjectUtils.isNotEmpty(money) && ObjectUtils.isNotNull(rule)) {
                Integer point1 = pointDetailRepository.findAllGetPoints(agent.getId(), agent.getCompanyId());
                Integer point2 = pointDetailRepository.findAllResumPoints(agent.getId(), agent.getCompanyId());
                if (new BigDecimal(money).compareTo(new BigDecimal(rule.getMinPrice())) == -1) {
                    response.setMsg("不能小于兑换最低限额").setCode("2");
                } else {
                    if (!ObjectUtils.isNotEmpty(point1)) {
                        point1 = 0;
                    }
                    if (!ObjectUtils.isNotEmpty(point2)) {
                        point2 = 0;
                    }
                    Integer point = point1 - point2;
                    if (point < 0) {
                        point = 0;
                    }
                    Double needpoint = Math.ceil(rule.getRmbForPoint() * new BigDecimal(money).setScale(2).doubleValue());
                    if (point >= needpoint.intValue()) {
                        try {
                            agentRepository.updatePoint(point - needpoint.intValue(), agent.getId());
                            agentRepository.updateScore(new BigDecimal(money), agent.getId(), agent.getCompanyId());
                            Earnings earnings = new Earnings();
                            earnings.setCusnamePhone(agent.getTelphone());
                            earnings.setCusname("");
                            earnings.setCusnamePhone("");
                            earnings.setMoney(new BigDecimal(money));
                            earnings.setType(5);
                            earnings.setAddTime(new Date());
                            earnings.setComId(agent.getCompanyId());
                            earnings.setDescript("积分兑换");
                            earnings.setUserId(agent.getId());
                            earnings.setAngentname(agent.getAgentName());

                            PointDetail pointDetail = new PointDetail();
                            pointDetail.setPoint(needpoint.intValue());
                            pointDetail.setUserId(agent.getId());
                            pointDetail.setType(6);
                            pointDetail.setStatus(2);
                            pointDetail.setDescript("兑换积分");
                            pointDetail.setAddTime(new Date());
                            pointDetail.setCompanyId(agent.getCompanyId());
                            try {
                                earningsRepository.save(earnings);
                                pointDetailRepository.save(pointDetail);
                                response.setMsg("积分兑换成功").setCode("1");
                            } catch (Exception e) {
                                response.setMsg("积分兑换失败").setCode("2");
                            }
                        } catch (Exception e) {
                            response.setMsg("积分兑换失败").setCode("2");
                        }
                    } else {
                        response.setMsg("积分不足无法兑换").setCode("2");
                    }
                }
            } else {
                response.setMsg("积分兑换失败").setCode("2");
            }
        });
        return response;
    }

    @PostMapping(value = "get_navigate_info")
    @CrossOrigin
    public BaseJsonResponse getNavigateInfo(String user_id, String token) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            Map<String, String> map = new HashMap<>();
            Enterprise enterprise = enterpriseRepository.findEnterpriseById(agent.getCompanyId());
            if (ObjectUtils.isNotNull(enterprise)) {
                map.put("key", "df546a50e9475878c39f203a8987b032");
                map.put("address", enterprise.getEnterAddress());
                String resp = HttpClientUtil.sendPostSSLRequest("https://restapi.amap.com/v3/geocode/geo", map, "utf-8");
                com.alibaba.fastjson.JSONObject object = JSON.parseObject(resp);
                String localtion = object.getJSONArray("geocodes").getJSONObject(0).getString("location");
                String[] localArray = localtion.split(",");
                NavigateData data = new NavigateData();
                data.setLongitude(localArray[0]);
                data.setLatitude(localArray[1]);
                data.setCompany_name(enterprise.getEnterName());
                response.setCode("1").setMsg("获取信息成功").setData(data);
            } else {
                response.setMsg("该企业不存在").setCode("2");
            }


        });
        return response;
    }


    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 获取爆款推荐商品列表
     *
     * @param user_id
     * @param token
     * @return
     * @throws ParseException
     * @throws AlipayApiException
     * @throws IOException
     */
    @PostMapping(value = "get_goodslist")
    @CrossOrigin
    public Object getGoodsList(String user_id, String token, Integer page, Integer limit) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            List<GoodsData> goodsDataList = new ArrayList<>();
            if (!ObjectUtils.isEmpty(agent)) {
                List<Commodity> goodsList = commodityRepository.findPageCommodityASC(0, 0, agent.getCompanyId(), 0, (page - 1) * limit, limit);
                if (goodsList.size() > 0) {
                    for (Commodity goods : goodsList) {
                        CommodityImg commodityImg = commodityImgRepository.findFirstByCommodityd(goods.getId());
                        GoodsData data = new GoodsData();
                        data.setGoodsId(goods.getId());
                        if (!ObjectUtils.isEmpty(commodityImg)) {
                            data.setImage(commodityImg.getCommodityImgUrl());
                        }
                        data.setLabel(goods.getLabel());
                        data.setLinkname(goods.getLinkname());
                        data.setSubtitle(goods.getActivityDescription());
                        data.setTitle(goods.getCommodityName());
                        goodsDataList.add(data);
                    }
                    response.setCode("1").setMsg("获取商品列表成功").setData(goodsDataList);
                } else {
                    response.setCode("0").setMsg("没有商品").setData(new ArrayList<>());
                }
                //GoodsData data = JsonUtils.readJsonFromClassPath("/static/json/goodslist.json",GoodsData.class); //从json文件中读取数据
            } else {
                response.setCode("0").setMsg("该用户没有绑定公司").setData(null);
            }
        });
        return response;
    }


    @Autowired
    private VoucherShareRepository voucherShareRepository;

    /**
     * 抵用券分享
     *
     * @return
     */
    @PostMapping(value = "all_share")
    @CrossOrigin
    public Object voucherShare(String user_id, String token, Integer sharetype, Integer goodsId) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            VoucherShare voucherShare = null;
            if (sharetype == 1) {
                voucherShare = voucherShareRepository.findVoucherShareByCompanyIdAndSharetype(agent.getCompanyId(), sharetype);
            } else if (sharetype == 2) {
                voucherShare = voucherShareRepository.findVoucherShareByCompanyIdAndGoodsIdAndSharetype(agent.getCompanyId(), goodsId, sharetype);
            }
            if (!ObjectUtils.isEmpty(voucherShare)) {
                VoucherShareData data = new VoucherShareData();
                data.setContext(voucherShare.getContext());
                data.setImage(voucherShare.getImage());
                if(voucherShare.getGoodsId() == 0) {
                    data.setLink_url(voucherShare.getLinkUrl() + "?user_id=" + agent.getId());
                }else{
                    data.setLink_url(voucherShare.getLinkUrl() + "?goods_id="+goodsId);
                }
                data.setContext(voucherShare.getContext());
                data.setTitle(voucherShare.getTitle());
                data.setUser_id(agent.getId());
                response.setCode("1").setMsg("成功").setData(data);
            } else {
                response.setCode("0").setMsg("没有数据").setData(null);
            }
        });
        return response;
    }

    /**
     * 抵用券
     */
    @PostMapping(value = "voucher")
    @CrossOrigin
    public Object vocher(String user_id, String token) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            String price = "50";
            PriceData data = new PriceData();
            data.setPrice(price);
            response.setData(data).setMsg("成功").setCode("1");
        });
        return response;
    }

    @Autowired
    private CouponUserRepository couponUserRepository;

    @Autowired
    private ReportCustomerRepository reportCustomerRepository;

    /**
     * 抵用券报备
     *
     * @return
     */
    @PostMapping(value = "voucher_report")
    @CrossOrigin
    public Object voucherReport(String user_id, String token, String cus_name, String cus_phone) throws
            ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            if (!ObjectUtils.isEmpty(cus_name) && !ObjectUtils.isEmpty(cus_phone)) {
                ReportCustomer customer = new ReportCustomer();
                customer.setAgentId(agent.getId());
                customer.setCustomerName(cus_name);
                customer.setCustomerPhone(cus_phone);
                customer.setReportTime(new Date());
                try {
                    reportCustomerRepository.save(customer);
                    response.setMsg("报备成功").setCode("1");
                } catch (Exception e) {
                    response.setMsg("报备失败").setCode("0");
                }
            } else {
                response.setMsg("信息不完整").setCode("0");
            }

        });
        return response;
    }


    @Autowired
    private VoucherDetailRepository voucherDetailRepository;

    /**
     * 抵用券详情
     *
     * @return
     */
    @PostMapping(value = "voucher_detail")
    @CrossOrigin
    public Object voucherDetail(String user_id, String token) throws ParseException, AlipayApiException {
        BaseJsonResponse response = new BaseJsonResponse();
        verifyUser(user_id, token, response, o -> {
            Agent agent = (Agent) o;
            VoucherDetail voucherDetail = voucherDetailRepository.findVoucherDetailByCompanyId(agent.getCompanyId());
            if (!ObjectUtils.isEmpty(voucherDetail)) {
                VoucherDetailData data = new VoucherDetailData();
                data.setContext(voucherDetail.getContext());
                data.setStart_time(DateUtils.dateFormat(voucherDetail.getStartTime(), DateUtils.DATE_PATIERN_DOT));
                data.setEnd_time(DateUtils.dateFormat(voucherDetail.getEndTime(), DateUtils.DATE_PATIERN_DOT));
                List<VoucherUser> userList = voucherUserRepository.findAll();
                if(userList.size() > 200){
                    data.setUser_count(String.valueOf(userList.size()));
                }else{
                    data.setUser_count("78");
                }
                data.setPrice(voucherDetail.getPrice());
                data.setTitle(voucherDetail.getTitle());
                response.setCode("1").setMsg("获取详情页成功").setData(data);
            } else {
                response.setCode("0").setMsg("没有详情页").setData(null);
            }
        });
        return response;
    }

    /**
     * 获取领券
     *
     * @return
     */
    @PostMapping(value = "get_coupon")
    public Object getCoupon(String user_id, String telphone,String address) {
        BaseJsonResponse response = new BaseJsonResponse();
        if (!ObjectUtils.isEmpty(user_id) && !ObjectUtils.isEmpty(telphone)&&!ObjectUtils.isEmpty(address)) {
            DefaultEnter defaultEnter = defaultEnterRepository.findDefaultEnterByUserId(user_id);
            if (!ObjectUtils.isEmpty(defaultEnter)) {
                CouponUser user = new CouponUser();
                user.setCouponPhone(telphone);
                user.setCompanyId(defaultEnter.getCompayId());
                Agent agent = agentRepository.findAgentByTelphone(defaultEnter.getUserId());
                if (!ObjectUtils.isEmpty(agent)) {
                    user.setShareUser(agent.getAgentName());
                    user.setSharePhone(agent.getTelphone());
                    user.setCouponPhone(telphone);
                    user.setGetTime(new Date());
                    user.setAddress(address);
                    user.setCompanyId(agent.getCompanyId());
                    couponUserRepository.save(user);
                    response.setCode("1").setMsg("领取成功").setData(null);
                }

            } else {
                response.setCode("0").setMsg("信息错误").setData(null);
            }
        } else {
            response.setCode("0").setMsg("信息错误").setData(null);
        }

        return response;
    }


    /**
     * 添加优惠券用户
     */
    @PostMapping("addvoucheruser")
    public BaseJsonResponse addVoucherUser(Integer user_id,String user_name,String phone,String address){
        BaseJsonResponse response = new BaseJsonResponse();
        if(!ObjectUtils.isEmpty(user_id)&&!ObjectUtils.isEmpty(phone))
        {
            String code = AlicomDysmsUtil.getCode();
            VoucherUser user = new VoucherUser();
            user.setUserId(user_id);
            user.setUserName(user_name);
            user.setTelphone(phone);
            user.setGetTime(new Date());
            user.setVerifyCode(code);
            user.setAddress(address);
            VoucherUser existuser = voucherUserRepository.findVoucherUserByTelphone(phone);
            if(!ObjectUtils.isEmpty(existuser)){
                    response.setCode("1").setMsg("您已经领取优惠券").setData(null);
            }else{
                try {
                    voucherUserRepository.save(user);
                    response.setCode("0").setMsg("领取成功").setData(null);
                    AlicomDysmsUtil.sendSms(phone,code,"SMS_164277629");
                }catch (Exception e){
                    response.setCode("2").setMsg("领取失败").setData(null);
                }
            }
        }else{
            response.setCode("4").setMsg("请将您的信息填写完整").setData(null);
        }

        return response;
    }
}
