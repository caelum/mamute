insert into User (id, moderator,karma,name, createdAt, email, password) values (1, false,0,'Gui', '20120101', 'guilherme.silveira@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (id, moderator,karma,name, createdAt, email, password) values (1000, false,0,'Anonimo', '20120101', 'anonimo@anonimo.com', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d51');
insert into User (id, moderator,karma,name, createdAt, email, password) values (2, false,0,'Leo', '20120102', 'leonardo.wolter@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (id, moderator,karma,name, createdAt, email, password) values (3, false,0,'Chico', '20120103', 'francisco.sokol@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (id, moderator,karma,name, createdAt, email, password) values (4, false,0,'Art', '20120104', 'artur.adam@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (id, moderator,karma,name, createdAt, email, password) values (5, false,0,'Val', '20120104', 'ricardo.valeriano@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (id, moderator,karma,name, createdAt, email, password) values (6, false,0,'Fe', '20120104', 'felipe.torres@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (id, moderator,karma,name, createdAt, email, password) values (7, true,0,'Moderator', '20120101', 'moderator@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');

insert into QuestionInformation (id,createdAt, description, markedDescription, sluggedTitle, title, author_id) values (1,'20130101', 'Como faz para dançar gangnam style? blablablablablablablablablabla', 'Como faz para dançar gangnam style? blablablablablablablablablabla', 'como-faz-para-danar-gangnam-style-blablablablablablablablablabla', 'OPPA? blablablablablablablablablablablabla', 1);
insert into QuestionInformation (id,createdAt, description, markedDescription, sluggedTitle, title, author_id) values (2,'20130101', 'Como faz para assistir anime? blablablablablablablablablabla', 'Como faz para assistir anime? blablablablablablablablablabla', 'como-faz-para-assistir-anime-blablablablablablablablablabla','Death Note é maneiro? blablablablablablablablablablablablablablablabla', 2);
insert into QuestionInformation (id,createdAt, description, markedDescription, sluggedTitle, title, author_id) values (3,'20130101', 'Como faz para pegar o saleiro da mão de outra pessoa? blablablablablablablablablabla','Como faz para pegar o saleiro da mão de outra pessoa? blablablablablablablablablabla', 'como-faz-para-pegar-o-saleiro-da-mao-de-outra-pessoa-blablablablablablablablablabla', 'Saleiro voador pode? blablablablablablablablablablablablablabla', 3);
insert into QuestionInformation (id,createdAt, description, markedDescription, sluggedTitle, title, author_id) values (4,'20130101', 'Como faz para diminuir o alcoolismo quando ele começou aos 11 anos? blablablablablablablablablabla', 'Como faz para diminuir o alcoolismo quando ele começou aos 11 anos? blablablablablablablablablabla', 'como-faz-para-diminuir-o-alcolismo-quando-ele-comecou-aos-11-anos-blablablablablablablablablabla', 'Bêbado que nem uma porca pode? blablablablablablablablablablablabla', 4);
insert into QuestionInformation (id,createdAt, description, markedDescription, sluggedTitle, title, author_id) values (5,'20130101', 'Como faz para ter um humor um pouco melhor pelas manhãs? blablablablablablablablablablablabla', 'Como faz para ter um humor um pouco melhor pelas manhãs? blablablablablablablablablablablabla', 'como-faz-para-ter-um-humor-um-pouco-melhor-pelas-manhas-blablablablablablablablablabla', '7 hora da manhã já pode estar puto? blablablablablablablablablablablabla', 5);
insert into QuestionInformation (id,createdAt, description, markedDescription, sluggedTitle, title, author_id) values (6,'20130101', 'Como faz para parar de usar android e linux? blablablablablablablablablablablabla', 'Como faz para parar de usar android e linux? blablablablablablablablablablablabla', 'como-faz-para-parar-de-usar-android-e-linux-blablablablablablablablablablablabla', 'Usar celular e computador que funciona, pode? blablablablablablablablablablablabla', 6);

insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,1,'20130101',  1, 0,1);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,2,'20130101',  2, 0,2);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,3,'20130101', 3, 0,3);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,4,'20130101', 4, 0,4);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,5,'20130101', 5, 0,5);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,6,'20130101', 6, 0,6);

insert into Tag (id, createdAt, description, name, author_id) values (1, '20130101', 'Para questions relacionadas à dança koreana', 'danca-koreana', 1);
insert into Tag (id, createdAt, description, name, author_id) values (2, '20130101', 'Para questions relacionadas à anime', 'anime', 2);
insert into Tag (id, createdAt, description, name, author_id) values (3, '20130101', 'Para questions relacionadas ao temor de pegar o saleiro da mão dos outros', 'saleiro', 3);
insert into Tag (id, createdAt, description, name, author_id) values (4, '20130101', 'Para questions relacionadas à alcoolismo', 'alcoolismo', 4);
insert into Tag (id, createdAt, description, name, author_id) values (5, '20130101', 'Para questions relacionadas à pessoas mal-humoradas', 'mal-humor', 5);
insert into Tag (id, createdAt, description, name, author_id) values (6, '20130101', 'Para questions relacionadas à sistemas marotos', 'sistemas-marotos', 6);

insert into QuestionInformation_Tag (QuestionInformation_id, tags_id) values (1,1);
insert into QuestionInformation_Tag (QuestionInformation_id, tags_id) values (2,2);
insert into QuestionInformation_Tag (QuestionInformation_id, tags_id) values (3,3);
insert into QuestionInformation_Tag (QuestionInformation_id, tags_id) values (4,4);
insert into QuestionInformation_Tag (QuestionInformation_id, tags_id) values (5,5);
insert into QuestionInformation_Tag (QuestionInformation_id, tags_id) values (6,6);

insert into AnswerInformation(id, createdAt,description, markedDescription, author_id) values (1,now(), 'Como todo belo coreano faz', 'Como todo belo coreano faz', 3);
insert into AnswerInformation(id, createdAt,description, markedDescription, author_id) values (2,now(), 'Como toda bela coreana faz', 'Como toda bela coreana faz', 4);
insert into AnswerInformation(id, createdAt,description, markedDescription, author_id) values (3,now(), 'Como todo belo ocidental paga pau faz', 'Como todo belo ocidental paga pau faz', 2);
insert into Answer(voteCount, createdAt,author_id,question_id, information_id) values (0,now(),3, 1,1);
insert into Answer(voteCount, createdAt,author_id,question_id, information_id) values (0,now(),4, 1,2);
insert into Answer(voteCount, id, createdAt,author_id,question_id, information_id) values (0,3,now(),2, 2,3);
update Question set solution_id=3 where id=2;
