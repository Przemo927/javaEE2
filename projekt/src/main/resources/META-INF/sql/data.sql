INSERT INTO role VALUES ('admin','admin');
INSERT INTO role VALUES('user','user');
INSERT INTO user(active,email,password,username) VALUES (1,	'przemyslaw.swietoslawski@gmail.com',	'CDA07453A453B3902902E7EE42693511:1000:18BE0F09DAB208DB13551C4C2AB0782008EEA2CD61C62D3E50A73E4792766A71B8DCCC50453E10DFA7CD329B12A87BAAFB11F05DBAFC4A7064F7587FB3B7F6F7',	'Admin');
INSERT INTO user(active,email,password,username) VALUES (1,	'przemo927@gmail.com',	'E554D011144C5A60B6E43276AF0314F8:1000:948DB6D12D20075E61D458822EB1BF7C947D64D63152C428DDDDF21B1D7F3C0684AACA60D8642B0ABEAC2376493EC300070F5706208C668B39AFF8544EA1BB88',	'Przemek');
INSERT INTO user_role VALUES ('admin','Admin');
INSERT INTO user_role VALUES ('user','Przemek');
INSERT INTO discovery(description,downVote,name,timestamp,upVote,url,user_id) VALUES  ('Silownia', 0, 'Silownia', '2017-11-29 21:06:24', 0, 'http://www.silownia.pl', 1);
INSERT INTO securitykey(username,privateKey) VALUES ('Przemek','MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCKwkIS2A13rJpu1azbOZkkNkOczTV1UJME0i7/orbTDMjoYXhiFze5oTKwavysuM5O2p4gMzyT7JGH07mKCQ7UZNw36Dq2snh9PKWrh6ilxA3KfkRVRUtFr7m+XQJO+tzLbRY8k9/AfDTJygVrbHhIv7u3DRyvb0ewufayy3XNOgydtzUWYx0GTw1bb4aE7nUofARBCkMvlQUZPHRo9nAVu37BrxZfEnPqKPjXXnBNWSK92RXk1w/xazlPVpoZhXEjEgs/VjVuswm2nA9HS4ouVPp3bvH0dEogXU50u9Ku/TsyMLKQMUKXXzgbN0npcfaVZchER8I3Lj+/vbovpeBXAgMBAAECggEAcZSTWzNoyrYJb4IOTSB3n3GfpKFBCfdbcI3PlMcPGfCUH8txK/HPVm6BvHDGxWc80MUYvEjUZE03ztPbQwcpEWhB7q10KgoqoHDdN2wC+zVhCIiUzKu88pzl0NH3SskDpKx1kD6G39uLpkVsbENMrlfrguoDugK/zwRZ0H7Evah60b0P2EyOLcaS3lu9w1ScH8UL2dyfpM3l0M66arb1kVrrBCt7Mai/7oaJVZT346nfxoM2z8W6F1pbAXYbxXTo2p1LV63D/kz0bEk9fknKo9i9bBKW+RyXDSqoDTi1IJt4LIZf4ww4hbxYvrWLIu25P6Z64bpSIkMjkk0ZQ/EuCQKBgQDEFmpgp/1sRoVFa4fQR2GRUlQocTuAH/Z2ybgoc0NuVB7eM2x3wIs3YteR9/BMrYAexNdumv4/+7JYclXwsZg0zwHZbpNx36/DpaOCSQEWxhz1YkM3J0DaDCGhJYJtowqQzCe3JXaIdq926UUHOk04EVomF8ju9B0V0ChTsnB5RQKBgQC1J7NxF9zS7iQTav+10y2c0ifmDGHyIY9W5fNFaQoXgFYn1cvuPv2nUogJ1wGJthFbljqBwrjTVVbAoiDkODUELCOZJDImvydz0L1n/1RsxBvbu7U857UThTH4LzuskwnvfAbyfHxlUWG+HegnZRwRUdlQVMG7HfjcOExALao26wKBgQCc0UBp5G9r9EoI5qJLE4sX6TuczFHFecpl3ZUF5Ih855F4KrAIZVfxB39XIW5wJCFeehXAQvwqt2Q+D9JjF+Sd9x+HgEMywq2SThmhzDiuuenW5KrUpPPcbe9HLPROMiH1AxCQC8nsAN76vjuSODIhMN9jr4nmcBwCoeJebUo98QKBgDfFxlAuSZVDLiYwThPbtXJPnWsNKuaSAIB/gVtAUJQVtfDDuOpbyFpIZYup/1Y3kLaC1PZ4TdD6uB/MipXY3DGxqTAP3cTiGzJj9Lxc9tAZvH31F4Jtz+j3m//OAvBJwMhThgwH2YCUAqS2rm/aiD6o1WAPCCokuyaJyqgDpyELAoGBAJ6Hm4QgEsLfW27LaXHVSwWMLmS8SLDFNhrzzRLckVAXSlVVereUxqQM91M4uUQGJuD2Wchbx4t56ltTmWm00wqAiVPr0cyUbbsehjPGAYv7ja7TNdY0yQH+uwE58SwFtNcrwQL3AxBpGYMgT/mGkgNzF2wRc/zhOOsX/MuXnSh3');
LOAD DATA LOCAL INFILE 'bad-words_regex.txt' INTO TABLE forbiddenwords LINES TERMINATED BY '\r\n'(word);