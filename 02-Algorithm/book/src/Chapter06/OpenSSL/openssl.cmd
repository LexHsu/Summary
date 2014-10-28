echo off

echo 构建目录

mkdir certs
mkdir crl
mkdir newcerts
mkdir private


echo 构建文件
echo 0>index.txt
echo 01>serial

echo 构建随机数
openssl rand -out private/.rand 1000

echo 产生私钥
openssl genrsa -des3 -out private/ca.key.pem 2048

echo 生成根证书请求 ca.csr
openssl req -new -key private/ca.key.pem -out private/ca.csr -subj "/C=CN/ST=BJ/L=BJ/O=zlex/OU=zlex/CN=root.zlex.org"

echo 签发根证书 ca.cer
openssl x509 -req -days 10000 -sha1 -extensions v3_ca -signkey private/ca.key.pem -in private/ca.csr -out certs/ca.cer

echo 根证书转换 ca.p12
openssl pkcs12 -export -clcerts -in certs/ca.cer -inkey private/ca.key.pem -out certs/ca.p12

echo 颁发服务器证书
openssl genrsa -out private/server.key.pem 2048

echo 生成服务器证书请求 server.csr
openssl req -new -key private/server.key.pem -out private/server.csr -subj "/C=CN/ST=BJ/L=BJ/O=zlex/OU=zlex/CN=server.zlex.org"

echo 签发服务器证书 server.cer
openssl x509 -req -days 3650 -sha1 -extensions v3_req -CA certs/ca.cer -CAkey private/ca.key.pem -CAserial ca.srl -CAcreateserial -in private/server.csr -out certs/server.cer

echo 服务器证书转换 server.p12
openssl pkcs12 -export -clcerts -in certs/server.cer -inkey private/server.key.pem -out certs/server.p12

echo 产生客户私钥
openssl genrsa -des3 -out private/client.key.pem 2048

echo 生成客户证书请求 client.csr
openssl req -new -key private/client.key.pem -out private/client.csr -subj "/C=CN/ST=BJ/L=BJ/O=zlex/OU=zlex/CN=client.zlex.org"

echo 签发客户证书 client.cer
openssl ca -in private/client.csr -days 3650 -out certs/client.cer -cert certs/ca.cer -keyfile private/ca.key.pem -notext

echo 客户证书转换 client.p12
openssl pkcs12 -export -inkey private/client.key.pem -in certs/client.cer -out certs/client.p12

echo 根密钥库转换 ca.keystore
keytool -importkeystore -v -srckeystore certs/ca.p12 -srcstorepass 123456 -destkeystore certs/ca.keystore -srcstoretype pkcs12 -deststorepass 123456
keytool -list -keystore certs/ca.keystore -v -storepass 123456

echo 服务器密钥库转换 server.keystore
keytool -importkeystore -v -srckeystore certs/server.p12 -srcstorepass 123456 -destkeystore certs/server.keystore -srcstoretype pkcs12 -deststorepass 123456
keytool -list -keystore certs/server.keystore -v -storepass 123456

echo 客户密钥库转换 client.keystore
keytool -importkeystore -v -srckeystore certs/client.p12 -srcstorepass 123456 -destkeystore certs/client.keystore -srcstoretype pkcs12 -deststorepass 123456
keytool -list -keystore certs/client.keystore -v -storepass 123456

pause

echo on