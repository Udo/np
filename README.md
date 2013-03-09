np
==

np FastCGI runtime

(work in progress)

How to Install
==

The fcgi/bin folder contains the actual runtime, use srv.sh to start the server. To configure nginx to use np, follow this 
recipe:

1) open /etc/nginx/nginx.conf in a text editor
2) add this FCGI handler to your config. There should already be one for PHP, np's will be just the same and you can add it 
directly below that:

      location ~ \.np$ {
          fastcgi_pass   127.0.0.1:9001;
          fastcgi_index  index.np;
          fastcgi_param  SCRIPT_FILENAME  /srv/www/htdocs/$host$fastcgi_script_name;
          include        fastcgi_params;
      }

Important: change "/srv/www/htdocs/$host$fastcgi_script_name" to wherever your docs are served from. If in doubt, just copy the 
same line from your existing PHP config.

3) locate your "index" directive and add "index.np" to that. Afterwards, it should look similar (but not necessarily identical) 
to this:

      index  index.php index.np index.html index.htm;

4) if you have a try_files directive, add index.np to that as well. For example, like this:

      try_files $uri $uri/ index.php index.np;

5) save, and restart nginx with "/etc/init.d/nginx reload" or whatever is appropriate for your system. Review and then start fcgi/bin/srv.sh.
If you plan to keep np running for some time, use the Unix screen command to generate a persistent session or resort to making a
regular service script for it (not included here at this time).

6) keep in mind that for this to work, the np server has to be actually running. As long as srv.sh isn't running, you'll get a 
"Bad Gateway" message from nginx.

Playing around with np
==
As the runtime matures, I'll update tutorials and documentation at http://np-lang.org/
