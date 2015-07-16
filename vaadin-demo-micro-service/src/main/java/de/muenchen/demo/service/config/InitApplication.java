/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.muenchen.demo.service.config;

import de.muenchen.demo.service.domain.AuthPermId;
import de.muenchen.demo.service.domain.Authority;
import de.muenchen.demo.service.domain.AuthorityPermission;
import de.muenchen.demo.service.domain.AuthorityPermissionRepository;
import de.muenchen.demo.service.domain.AuthorityRepository;
import de.muenchen.demo.service.domain.Permission;
import de.muenchen.demo.service.domain.PermissionRepository;
import de.muenchen.demo.service.domain.User;
import de.muenchen.demo.service.domain.UserAuthId;
import de.muenchen.demo.service.domain.UserAuthority;
import de.muenchen.demo.service.domain.UserAuthorityRepository;
import de.muenchen.demo.service.domain.UserRepository;
import de.muenchen.demo.service.rest.AccountController;
import de.muenchen.demo.service.rest.PassController;
import de.muenchen.demo.service.rest.AdresseController;
import de.muenchen.demo.service.rest.AuthorityController;
import de.muenchen.demo.service.rest.AuthorityPermissionController;
import de.muenchen.demo.service.rest.BuergerController;
import de.muenchen.demo.service.rest.CompanyBaseInfoController;
import de.muenchen.demo.service.rest.MandantController;
import de.muenchen.demo.service.rest.PermissionController;
import de.muenchen.demo.service.rest.StaatsangehoerigkeitController;
import de.muenchen.demo.service.rest.UserAuthorityController;
import de.muenchen.demo.service.rest.UserController;
import de.muenchen.demo.service.rest.WohnungController;
import de.muenchen.demo.service.util.IdService;
import static java.lang.Boolean.TRUE;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author praktikant.tmar
 */
public class InitApplication {
    @Autowired
    UserRepository usersRepo;
    @Autowired
    AuthorityRepository authRepo;
    @Autowired
    PermissionRepository permRepo;
    @Autowired
    UserAuthorityRepository userAuthRepo;
    @Autowired
    AuthorityPermissionRepository authPermRepo;
    public InitApplication() {

    }

    public InitApplication(UserRepository usersRepo, AuthorityRepository authRepo, PermissionRepository permRepo, UserAuthorityRepository userAuthRepo, AuthorityPermissionRepository authPermRepo) {
        this.usersRepo = usersRepo;
        this.authRepo = authRepo;
        this.permRepo = permRepo;
        this.userAuthRepo = userAuthRepo;
        this.authPermRepo = authPermRepo;
    }

    public void init() {
        User user = new User();
        user.setEmail("hans@muenchen.de");
        user.setPassword("test");
        user.setUsername("hans");
        user.setOid(IdService.next());
        user.setEnabled(TRUE);
        usersRepo.save(user);

        Authority auth = new Authority();
        auth.setAuthority("ADMIN");
        auth.setOid(IdService.next());
        authRepo.save(auth);

        List<String> list = new ArrayList();
        for (Method method : BuergerController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }

        for (Method method : AdresseController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : StaatsangehoerigkeitController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : WohnungController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : AuthorityController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : UserController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : CompanyBaseInfoController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : PermissionController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : UserAuthorityController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : AuthorityPermissionController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : AccountController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : PassController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (Method method : MandantController.class.getDeclaredMethods()) {
            String name = method.getName();
            list.add("PERM_" + name);
        }
        for (String list1 : list) {
            Permission permission = new Permission();
            permission.setPermision(list1);
            permission.setOid(IdService.next());
            permRepo.save(permission);

            AuthorityPermission authPerm = new AuthorityPermission();
            AuthPermId idA = new AuthPermId(permission, auth);
            authPerm.setId(idA);
            authPermRepo.save(authPerm);

        }
        UserAuthority userAuth = new UserAuthority();
        UserAuthId id = new UserAuthId(user, auth);

        userAuth.setId(id);

        userAuthRepo.save(userAuth);
    }

}
