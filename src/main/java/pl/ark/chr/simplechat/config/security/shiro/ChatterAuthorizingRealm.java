package pl.ark.chr.simplechat.config.security.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.ark.chr.simplechat.domain.ChatterUser;
import pl.ark.chr.simplechat.repository.RoleRepository;
import pl.ark.chr.simplechat.service.ChatterUserService;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Arek on 2017-06-22.
 */
@Component
public class ChatterAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private ChatterUserService chatterUserService;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username;
        try {
            username = (String) principalCollection.fromRealm(getName()).iterator().next();
        } catch (NoSuchElementException ex) {
            return null;
        }

        Optional<ChatterUser> userOptional = chatterUserService.getByUsername(username);

        if (userOptional.isPresent()) {
            ChatterUser user = userOptional.get();
            Set<String> roles = new HashSet<>();
            roles.add(roleRepository.findOne(user.getRole().getId()).getName());
            SimpleAuthorizationInfo authInfo = new SimpleAuthorizationInfo(roles);
            authInfo.setStringPermissions(roles);

            return authInfo;
        }

        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken authToken = (UsernamePasswordToken) authenticationToken;

        Optional<ChatterUser> user = chatterUserService.getByUsername(authToken.getUsername());

        if (!user.isPresent()) {
            throw new AuthenticationException("Account does not exists");
        }

        ChatterUser loggedUser = user.get();

        return new SimpleAuthenticationInfo(loggedUser.getUsername(), loggedUser.getPassword(), getName());
    }
}
