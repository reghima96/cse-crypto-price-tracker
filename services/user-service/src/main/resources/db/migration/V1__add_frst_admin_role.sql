INSERT INTO user_roles (user_id, role)
SELECT id, 'ADMIN'
FROM cpt_users
WHERE email = 'cse@user.com';