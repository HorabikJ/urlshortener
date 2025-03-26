package pl.jacekhorabik.urlshortener.security;


// @Configuration
// @EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
// @KeycloakConfiguration
// public class KeycloakConfig extends KeycloakWebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        http.authorizeRequests()
//                .antMatchers(HttpMethod.GET,"/api/v1/projects").hasAnyRole("user")
//                .antMatchers(HttpMethod.GET,"/api/v1/project").hasAnyRole("admin")
//                //.antMatchers("/api/v1/project").hasAnyRole("user","admin")
//                .anyRequest()
//                .permitAll();
//
// http.csrf().disable().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
//    }
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        KeycloakAuthenticationProvider keycloakAuthenticationProvider =
// keycloakAuthenticationProvider();
//        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
//        auth.authenticationProvider(keycloakAuthenticationProvider);
//    }
//
//    @Bean
//    @Override
//    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
//        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
//    }
//
//    @Bean
//    public KeycloakConfigResolver KeycloakConfigResolver() {
//        return new KeycloakSpringBootConfigResolver();
//    }
// }
