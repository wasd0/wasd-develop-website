# wasd-develop-website
Personal website with rest services

<h4>Stack:
<list>
  <ol></ol>
  <ol>* Spring[boot, web, data, security]</ol>
  <ol>* Postgresql</ol>
  <ol>* Lombok</ol>
</list>
</h4>
<hr/>

<h3>1. Set your data base properties</h1>
<hr/>
<p><b>src/main/resources/application.yaml</b></p>

![image](https://github.com/wasd0/wasd-develop-website/assets/84603952/c9078369-4fb0-4b4d-82f6-b2235904c136)

<br/>
<hr/>
<h3>2. Fill data base (roles and authorities)</h1>
<hr/>

* <b> INSERT INTO public.authorities (id, name) VALUES (1, 'UPDATE');
* INSERT INTO public.authorities (id, name) VALUES (2, 'READ');
* INSERT INTO public.authorities (id, name) VALUES (3, 'CREATE');
* INSERT INTO public.authorities (id, name) VALUES (4, 'DELETE'); </b>
  <br/>
  <br/>
* <b>INSERT INTO public.roles (id, name) VALUES (1, 'ROLE_USER');
* INSERT INTO public.roles (id, name) VALUES (2, 'ROLE_ADMIN'); </b>
<br/>

*  <b>INSERT INTO public.roles_authorities (role_id, authority_id) VALUES (1, 2);
*  INSERT INTO public.roles_authorities (role_id, authority_id) VALUES (2, 4);
*  INSERT INTO public.roles_authorities (role_id, authority_id) VALUES (2, 3);
*  INSERT INTO public.roles_authorities (role_id, authority_id) VALUES (2, 2);
*  INSERT INTO public.roles_authorities (role_id, authority_id) VALUES (2, 1);
*  INSERT INTO public.roles_authorities (role_id, authority_id) VALUES (1, 3); <b/>
