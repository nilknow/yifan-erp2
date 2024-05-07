package com.nilknow.yifanerp2.controller;

import com.nilknow.yifanerp2.config.security.UserIdHolder;
import com.nilknow.yifanerp2.dto.LoginUserDto;
import com.nilknow.yifanerp2.dto.RouteDto;
import com.nilknow.yifanerp2.entity.LoginUser;
import com.nilknow.yifanerp2.entity.Route;
import com.nilknow.yifanerp2.exception.ResException;
import com.nilknow.yifanerp2.service.LoginUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private LoginUserService loginUserService;
    @Resource
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/info")
    public Res<LoginUserDto> info(HttpServletResponse response) throws ResException {
        Long userId = UserIdHolder.get();
        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }
        Optional<LoginUser> userOpt = loginUserService.findById(userId);
        if (userOpt.isPresent()) {
            LoginUser loginUser = userOpt.get();
            List<Route> routes = loginUser.getRoutes();
            List<RouteDto> routeDtoList = routes.stream()
                    .map(x -> new RouteDto(x.getId(), x.getPath(), x.getName())).toList();
            LoginUserDto loginUserDto = new LoginUserDto();
            loginUserDto.setRoutes(routeDtoList);
            return new Res<LoginUserDto>().success(loginUserDto);
        } else {
            throw new ResException("No user with id " + userId);
        }
    }

//    @GetMapping("/route/tree")
//    public Res<RouteDto> routeTree(HttpServletResponse response) throws ResException {
//        Long userId = UserIdHolder.get();
//        if (userId == null) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return new Res<RouteDto>().fail("You should login first");
//        }
//        List<LevelRoute> orderRoutes = jdbcTemplate.queryForList("select r1.name l1_name,r1.path l1_path,\n" +
//                "\tr2.name l2_name,r2.path l2_path,\n" +
//                "\tr3.name l3_name,r3.path l3_path,\n" +
//                "\tr4.name l4_name,r4.path l4_path\n" +
//                "\tfrom route r1 \n" +
//                "\t\tleft join route r2\n" +
//                "\ton r1.id=r2.parent_id\n" +
//                "\t\tleft join route r3\n" +
//                "\ton r2.id=r3.parent_id\n" +
//                "\t\tleft join route r4\n" +
//                "\ton r3.id=r4.parent_id\n" +
//                "where r1.id=1\n" +
//                "order by r4.path,r3.path,r2.path,r1.path", LevelRoute.class);
//        Map<String, RouteDto> cache = new HashMap<>();
//        for (LevelRoute route : orderRoutes) {
//            String level = StringUtils.hasText(route.getL4Path()) ?
//                    "l4" :
//                    StringUtils.hasText(route.getL3Path()) ?
//                            "l3" :
//                            StringUtils.hasText(route.getL2Path()) ?
//                                    "l2" :
//                                    "l1";
//            String key;
//            RouteDto val ;
//            switch (level) {
//                case "l4" -> {
//                    key = route.getL4Path();
//                    val = new RouteDto();
//                    val.setId(route.getL4Id());
//                    val.setName(route.getL4Name());
//                    val.setPath(route.getL4Path());
//                }
//                case "l3" -> {
//                    key = route.getL3Path();
//                    val = new RouteDto();
//                    val.setId(route.getL3Id());
//                    val.setName(route.getL3Name());
//                    val.setPath(route.getL3Path());
//                }
//            }
//            cache.put(key, new RouteDto());
//        }
//
//        RouteDto dto = new RouteDto();
//        for (LevelRoute route : orderRoutes) {
//            if (!StringUtils.hasText(route.l2Name)) {
//                dto.setId(route.l1Id);
//                dto.setName(route.l1Name);
//                dto.setPath(route.l1Path);
//                dto.setChildren(new ArrayList<>());
//            } else if(!StringUtils.hasText(route.l3Name)) {
//                List<RouteDto> l2 = dto.getChildren();
//                RouteDto l2Dto = new RouteDto();
//                l2Dto.setId(route.l2Id);
//                l2Dto.setName(route.l2Name);
//                l2Dto.setPath(route.l2Path);
//                l2Dto.setChildren(new ArrayList<>());
//                l2.add(l2Dto);
//            } else if (!StringUtils.hasText(route.l4Name)) {
//
//            }
//        }
//        return null;
//    }
//
//    @Data
//    @NoArgsConstructor
//    @AllArgsConstructor
//    private static class LevelRoute {
//        private Long l1Id;
//        private String l1Name;
//        private String l1Path;
//        private Long l2Id;
//        private String l2Name;
//        private String l2Path;
//        private Long l3Id;
//        private String l3Name;
//        private String l3Path;
//        private Long l4Id;
//        private String l4Name;
//        private String l4Path;
//    }
}
