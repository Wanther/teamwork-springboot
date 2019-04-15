UPDATE sys_user SET PASSWORD=REPLACE(PASSWORD, '$2y$10', '$2a$10');
UPDATE oauth_client SET secret=REPLACE(secret, '$2y$10', '$2a$10');
