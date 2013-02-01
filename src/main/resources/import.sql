insert into User (createdAt, email, password) values ('20120101', 'guilherme.silveira@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (createdAt, email, password) values ('20120102', 'leonardo.wolter@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (createdAt, email, password) values ('20120103', 'francisco.sokol@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (createdAt, email, password) values ('20120104', 'artur.adam@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (createdAt, email, password) values ('20120104', 'ricardo.valeriano@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');
insert into User (createdAt, email, password) values ('20120104', 'felipe.torres@caelum.com.br', '2145a8274d61bd4377c8f98c2d8ab4c5ca93243a61ffddd03885fe990a740d57');

insert into Question (createdAt, description, sluggedTitle, title, author_id, views) values ('20130101', 'Como faz para dançar gangnam style? blablablablablablablablablabla', 'como-faz-para-danar-gangnam-style-blablablablablablablablablabla', 'OPPA? blablablablablablablablablablablabla', 1, 0);
insert into Question (id,createdAt, description, sluggedTitle, title, author_id, views) values (2,'20130101', 'Como faz para assistir anime? blablablablablablablablablabla', 'como-faz-para-assistir-anime-blablablablablablablablablabla','Death Note é maneiro? blablablablablablablablablablablablablablablabla', 2, 0);
insert into Question (createdAt, description, sluggedTitle, title, author_id, views) values ('20130101', 'Como faz para pegar o saleiro da mão de outra pessoa? blablablablablablablablablabla', 'como-faz-para-pegar-o-saleiro-da-mao-de-outra-pessoa-blablablablablablablablablabla', 'Saleiro voador pode? blablablablablablablablablablablablablabla', 3, 0);
insert into Question (createdAt, description, sluggedTitle, title, author_id, views) values ('20130101', 'Como faz para diminuir o alcoolismo quando ele começou aos 11 anos? blablablablablablablablablabla', 'como-faz-para-diminuir-o-alcolismo-quando-ele-comecou-aos-11-anos-blablablablablablablablablabla', 'Bêbado que nem uma porca pode? blablablablablablablablablablablabla', 4, 0);
insert into Question (createdAt, description, sluggedTitle, title, author_id, views) values ('20130101', 'Como faz para ter um humor um pouco melhor pelas manhãs? blablablablablablablablablablablabla', 'como-faz-para-ter-um-humor-um-pouco-melhor-pelas-manhas-blablablablablablablablablabla', '7 hora da manhã já pode estar puto? blablablablablablablablablablablabla', 5, 0);
insert into Question (createdAt, description, sluggedTitle, title, author_id, views) values ('20130101', 'Como faz para parar de usar android e linux? blablablablablablablablablablablabla', 'como-faz-para-parar-de-usar-android-e-linux-blablablablablablablablablablablabla', 'Usar celular e computador que funciona, pode? blablablablablablablablablablablabla', 6, 0);

insert into Answer(createdAt,text,author_id,question_id) values (now(), 'Como todo belo coreano faz', 3, 1);
insert into Answer(createdAt,text,author_id,question_id) values (now(), 'Como toda bela coreana faz', 4, 1);
insert into Answer(id, createdAt,text,author_id,question_id) values (3,now(), 'Como todo belo ocidental paga pau faz', 2, 2);
update Question set solution_id=3 where id=2;
update Answer set htmlText = text;