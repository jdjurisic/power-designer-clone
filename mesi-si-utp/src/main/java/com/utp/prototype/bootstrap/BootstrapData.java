package com.utp.prototype.bootstrap;

import com.utp.prototype.entities.*;
import com.utp.prototype.entities.Team;
import com.utp.prototype.model.*;
import com.utp.prototype.repository.ProjectRepository;
import com.utp.prototype.repository.ModelRepository;
import com.utp.prototype.repository.TeamRepository;
import com.utp.prototype.repository.UserRepository;
import com.utp.prototype.entities.Project;
import com.utp.prototype.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class BootstrapData implements CommandLineRunner {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ModelRepository modelRepository;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Loading data...");

        Team team = new Team();
        team.setName("si2020 3+mesi");
        teamRepository.save(team);



        List<User> users = new ArrayList<>();

        User user = new User();
        user.setUsername("iva");
        String encryptedPass = passwordEncoder.encode("Iva123");
        user.setPassword(encryptedPass);
        user.setUserType(UserType.USER);
        user.setTeam(team);
        users.add(user);

        User user1 = new User();
        user1.setUsername("jovica");
        String encryptedPass1 = passwordEncoder.encode("Jovica123");
        user1.setPassword(encryptedPass1);
        user1.setUserType(UserType.ADMIN);
        user1.setTeam(team);
        users.add(user1);

        userRepository.saveAll(users);


        Project project = new Project();
        project.setTeam(team);
        project.setName("Designer Power");
        projectRepository.save(project);

        Project project2 = new Project();
        project2.setTeam(team);
        project2.setName("Svemocni Dizajner");
        projectRepository.save(project2);

        Team team2 = new Team();
        team2.setName("test_team");
        teamRepository.save(team2);

        Project project3 = new Project();
        project3.setTeam(team2);
        project3.setName("test_project");
        projectRepository.save(project3);



        User user2 = new User();
        user2.setUsername("dzo");
        String encryptedPass2 = passwordEncoder.encode("Dzo123");
        user2.setPassword(encryptedPass2);
        user2.setUserType(UserType.ADMIN);
        user2.setTeam(team2);
        users.add(user2);

        userRepository.save(user2);

        //Kreiranje novih modela:

        // Dodavanje default requirementa:
        Model model1 = new Model();
        model1.setProject(project); // Dodaje se na Designer Power
        model1.setMongoKey("60778caa7dbc633a2accadfd");
        model1.setName("Rqm1");
        model1.setModelType(ModelType.REQUIREMENT);
        modelRepository.save(model1);

        Model model2 = new Model();
        model2.setProject(project);
        model2.setMongoKey("60778cb87dbc633a2accadfe");
        model2.setName("Rqm2");
        model2.setModelType(ModelType.REQUIREMENT);
        modelRepository.save(model2);

        Model model3 = new Model();
        model3.setProject(project2);  // Dodaje se na Svemocni Dizajner
        model3.setMongoKey("607798527dbc633a2accadff");
        model3.setName("Rqm3");
        model3.setModelType(ModelType.REQUIREMENT);
        modelRepository.save(model3);

        Model model4 = new Model();
        model4.setProject(project3);
        model4.setMongoKey("607788b8b0db835f52b6c3de");
        model4.setName("Rqm4");
        model4.setModelType(ModelType.REQUIREMENT);
        modelRepository.save(model4);

        // Dodavanje default UseCase-a:
        Model model5 = new Model();
        model5.setProject(project3);
        model5.setMongoKey("609166cfb503144aef978167");
        model5.setName("UseCaseModel5");
        model5.setModelType(ModelType.USE_CASE);
        modelRepository.save(model5);

        Model model6 = new Model();
        model6.setProject(project2);
        model6.setMongoKey("609166e6b503144aef978168");
        model6.setName("UseCaseModel6");
        model6.setModelType(ModelType.USE_CASE);
        modelRepository.save(model6);


        // Dodavanje default classnog:
        Model model7 = new Model();
        model7.setProject(project3);
        model7.setMongoKey("609557d284d32d404515840");
        model7.setName("KlasniModel7");
        model7.setModelType(ModelType.CLASS);
        modelRepository.save(model7);


        /*                            Rute                             **/


        // Otkomentarisati\zakomentarisati u zavisnosti da li broker trenutno radi!
        registerProjectService();
        registerUserService();
        registerTeamService();
        registerAuthService();
        registerModelService();

    }

    private void registerProjectService() {
        SService service = new SService();
        service.setName("project");
        service.setPort(8082);
        service.setUrl("http://localhost:");

        List<Method> methodList = new ArrayList<>();
        methodList.add(new Method(
                "saveProject",
                "/save",
                HttpMethod.POST,
                Collections.singletonList(new Parameter("project", "Project", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("project", "Project", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "updateProject",
                "/update",
                HttpMethod.PUT,
                Arrays.asList(  new Parameter("projectId", "Long", ParameterType.URL_PARAM),
                                new Parameter("project", "Project", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("projectId", "Long", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "deleteProject",
                "/delete",
                HttpMethod.DELETE,
                Collections.singletonList(new Parameter("projectId", "Long", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "getProject",
                "/get",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("projectId", "Long", ParameterType.URL_PARAM)),
                Collections.singletonList(new Parameter("project", "Project", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "allProject",
                "/all",
                HttpMethod.GET,
                Collections.emptyList(),
                Arrays.asList(new Parameter("project","Project", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "addModelToProject",
                "/addModelToProject",
                HttpMethod.POST,
                Arrays.asList(  new Parameter("modelId", "String", ParameterType.URL_PARAM),
                                new Parameter("projectId", "Long", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        //service.setMethods(methodList);




        RestTemplate restTemplate = new RestTemplate();


        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(Method m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,Method.class);
        }

        System.out.println("RestTemplate za Project deo servisa je poslat brokeru!");



    }

    private void registerUserService() {
        SService service = new SService();
        service.setName("user");
        service.setPort(8082);
        service.setUrl("http://localhost:");

        List<Method> methodList = new ArrayList<>();
        methodList.add(new Method(
                "saveUser",
                "/save",
                HttpMethod.POST,
                Collections.singletonList(new Parameter("user", "User", ParameterType.BODY_PARAM )),
                Collections.singletonList(new Parameter("user", "User", ParameterType.BODY_PARAM )),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "updateUser",
                "/update",
                HttpMethod.PUT,
                Arrays.asList(  new Parameter("userId", "Long", ParameterType.URL_PARAM),
                                new Parameter("user", "User", ParameterType.BODY_PARAM )),
                Collections.singletonList(new Parameter("userId", "Long", ParameterType.BODY_PARAM )),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "deleteUser",
                "/delete",
                HttpMethod.DELETE,
                Collections.singletonList(new Parameter("userId", "Long", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "getUser",
                "/get",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("userId", "Long", ParameterType.URL_PARAM)),
                Collections.singletonList(new Parameter("user", "User", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "allUser",
                "/all",
                HttpMethod.GET,
                Collections.emptyList(),
                Arrays.asList(new Parameter("user","User", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "getByUsernameUser",
                "/getByUsername",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("username", "String", ParameterType.URL_PARAM)),
                Arrays.asList(new Parameter("user","User", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        //service.setMethods(methodList);

        RestTemplate restTemplate = new RestTemplate();


        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(Method m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,Method.class);
        }

        System.out.println("RestTemplate za User deo servisa je poslat brokeru!");
    }

    private void registerTeamService() {
        SService service = new SService();
        service.setName("team");
        service.setPort(8082);
        service.setUrl("http://localhost:");

        List<Method> methodList = new ArrayList<>();
        methodList.add(new Method(
                "saveTeam",
                "/save",
                HttpMethod.POST,
                Collections.singletonList(new Parameter("team", "Team", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("team", "Team", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "updateTeam",
                "/update",
                HttpMethod.PUT,
                Arrays.asList(  new Parameter("teamId", "Long", ParameterType.URL_PARAM),
                                new Parameter("team", "Team", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("teamId", "Long", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "deleteTeam",
                "/delete",
                HttpMethod.DELETE,
                Collections.singletonList(new Parameter("teamId", "Long", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "getTeam",
                "/get",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("teamId", "Long", ParameterType.URL_PARAM)),
                Collections.singletonList(new Parameter("teamId", "Long", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "allTeam",
                "/all",
                HttpMethod.GET,
                Collections.emptyList(),
                Arrays.asList(new Parameter("team","Team", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN),
                service
        ));
        methodList.add(new Method(
                "addUserToTeam",
                "/addUserToTeam",
                HttpMethod.POST,
                Arrays.asList(  new Parameter("userId","Long", ParameterType.URL_PARAM),
                                new Parameter("teamId","Long", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN),
                service
        ));
        methodList.add(new Method(
                "addProjectToTeam",
                "/addProjectToTeam",
                HttpMethod.POST,
                Arrays.asList(  new Parameter("projectId","Long", ParameterType.URL_PARAM),
                                new Parameter("teamId","Long",  ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN),
                service
        ));
        //service.setMethods(methodList);

        RestTemplate restTemplate = new RestTemplate();


        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(Method m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,Method.class);
        }

        System.out.println("RestTemplate za Team deo servisa je poslat brokeru!");


    }
    private void registerAuthService() {
        SService service = new SService();
        service.setName("auth");
        service.setPort(8082);
        service.setUrl("http://localhost:");

        List<Method> methodList = new ArrayList<>();
        methodList.add(new Method(
                "login",
                "/login",
                HttpMethod.POST,
                Collections.singletonList(new Parameter("authenticationRequest", "AuthenticationRequest", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("authenticationResponse", "AuthenticationResponse", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "validate",
                "/validate",
                HttpMethod.POST,
                Collections.singletonList(new Parameter("jwt", "String", ParameterType.URL_PARAM)),
                Collections.singletonList(new Parameter("isValid", "Boolean", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        //service.setMethods(methodList);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(Method m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,Method.class);
        }

        System.out.println("RestTemplate za Authentication deo servisa je poslat brokeru!");
    }

    private void registerModelService() {
        SService service = new SService();
        service.setName("model");
        service.setPort(8082);
        service.setUrl("http://localhost:");


        List<Method> methodList = new ArrayList<>();
        methodList.add(new Method(
                "saveModel",
                "/save",
                HttpMethod.POST,
                Collections.singletonList(new Parameter("model", "Model", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("model", "Model", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "updateModel",
                "/update",
                HttpMethod.PUT,
                Arrays.asList(  new Parameter("modelId", "String", ParameterType.URL_PARAM),
                        new Parameter("model", "Model", ParameterType.BODY_PARAM)),
                Collections.singletonList(new Parameter("model", "Model", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "deleteModel",
                "/delete",
                HttpMethod.DELETE,
                Collections.singletonList(new Parameter("modelId", "String", ParameterType.URL_PARAM)),
                Collections.emptyList(),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "getModel",
                "/get",
                HttpMethod.GET,
                Collections.singletonList(new Parameter("modelId", "String", ParameterType.URL_PARAM)),
                Collections.singletonList(new Parameter("model", "Model", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN, UserType.USER),
                service
        ));
        methodList.add(new Method(
                "allModel",
                "/all",
                HttpMethod.GET,
                Collections.emptyList(),
                Arrays.asList(new Parameter("model","Model", ParameterType.BODY_PARAM)),
                Arrays.asList(UserType.ADMIN),
                service
        ));

        //service.setMethods(methodList);
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.postForObject("http://localhost:8081/service/save", service, SService.class);

        for(Method m : methodList){
            restTemplate.postForObject("http://localhost:8081/method/save",m,Method.class);
        }

        System.out.println("RestTemplate za Model deo servisa je poslat brokeru!");



    }
}
