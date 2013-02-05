insert into User (moderator,karma,name, createdAt, email, password) values (false,0,'Gui', '20120101', 'guilherme.silveira@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (moderator,karma,name, createdAt, email, password) values (false,0,'Leo', '20120102', 'leonardo.wolter@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (moderator,karma,name, createdAt, email, password) values (false,0,'Chico', '20120103', 'francisco.sokol@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (moderator,karma,name, createdAt, email, password) values (false,0,'Art', '20120104', 'artur.adam@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (moderator,karma,name, createdAt, email, password) values (false,0,'Val', '20120104', 'ricardo.valeriano@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (moderator,karma,name, createdAt, email, password) values (false,0,'Fe', '20120104', 'felipe.torres@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (moderator,karma,name, createdAt, email, password) values (true,0,'Moderator', '20120101', 'moderator@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');

insert into QuestionInformation (id,createdAt, description, sluggedTitle, title, author_id) values (1,'20130101', 'Como faz para dançar gangnam style? blablablablablablablablablabla', 'como-faz-para-danar-gangnam-style-blablablablablablablablablabla', 'OPPA? blablablablablablablablablablablabla', 1);
insert into QuestionInformation (id,createdAt, description, sluggedTitle, title, author_id) values (2,'20130101', 'Como faz para assistir anime? blablablablablablablablablabla', 'como-faz-para-assistir-anime-blablablablablablablablablabla','Death Note é maneiro? blablablablablablablablablablablablablablablabla', 2);
insert into QuestionInformation (id,createdAt, description, sluggedTitle, title, author_id) values (3,'20130101', 'Como faz para pegar o saleiro da mão de outra pessoa? blablablablablablablablablabla', 'como-faz-para-pegar-o-saleiro-da-mao-de-outra-pessoa-blablablablablablablablablabla', 'Saleiro voador pode? blablablablablablablablablablablablablabla', 3);
insert into QuestionInformation (id,createdAt, description, sluggedTitle, title, author_id) values (4,'20130101', 'Como faz para diminuir o alcoolismo quando ele começou aos 11 anos? blablablablablablablablablabla', 'como-faz-para-diminuir-o-alcolismo-quando-ele-comecou-aos-11-anos-blablablablablablablablablabla', 'Bêbado que nem uma porca pode? blablablablablablablablablablablabla', 4);
insert into QuestionInformation (id,createdAt, description, sluggedTitle, title, author_id) values (5,'20130101', 'Como faz para ter um humor um pouco melhor pelas manhãs? blablablablablablablablablablablabla', 'como-faz-para-ter-um-humor-um-pouco-melhor-pelas-manhas-blablablablablablablablablabla', '7 hora da manhã já pode estar puto? blablablablablablablablablablablabla', 5);
insert into QuestionInformation (id,createdAt, description, sluggedTitle, title, author_id) values (6,'20130101', 'Como faz para parar de usar android e linux? blablablablablablablablablablablabla', 'como-faz-para-parar-de-usar-android-e-linux-blablablablablablablablablablablabla', 'Usar celular e computador que funciona, pode? blablablablablablablablablablablabla', 6);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,1,'20130101',  1, 0,1);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,2,'20130101',  2, 0,2);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,3,'20130101', 3, 0,3);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,4,'20130101', 4, 0,4);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,5,'20130101', 5, 0,5);
insert into Question (voteCount, id,createdAt, author_id, views, information_id) values (0,6,'20130101', 6, 0,6);

insert into Answer(voteCount, createdAt,text, htmlText, author_id,question_id) values (0,now(), 'Como todo belo coreano faz', 'Como todo belo coreano faz', 3, 1);
insert into Answer(voteCount, createdAt,text, htmlText, author_id,question_id) values (0,now(), 'Como toda bela coreana faz', 'Como toda bela coreana faz', 4, 1);
insert into Answer(voteCount, id, createdAt,text, htmlText, author_id,question_id) values (0,3,now(), 'Como todo belo ocidental paga pau faz', 'Como todo belo ocidental paga pau faz', 2, 2);
update Question set solution_id=3 where id=2;
update Answer set htmlText = text;