--
--  Tigase Jabber/XMPP Server
--  Copyright (C) 2004-2017 "Tigase, Inc." <office@tigase.com>
--
--  This program is free software: you can redistribute it and/or modify
--  it under the terms of the GNU Affero General Public License as published by
--  the Free Software Foundation, either version 3 of the License.
--
--  This program is distributed in the hope that it will be useful,
--  but WITHOUT ANY WARRANTY; without even the implied warranty of
--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--  GNU Affero General Public License for more details.
--
--  You should have received a copy of the GNU Affero General Public License
--  along with this program. Look for COPYING file in the top folder.
--  If not, see http://www.gnu.org/licenses/.
--
--

run 'database/derby-schema-7-1-schema.sql';

-- LOAD FILE: database/derby-schema-7-1-schema.sql

-- QUERY START:
create table tig_offline_messages (
    msg_id bigserial,
    ts timestamp default now(),
    expired timestamp,
    sender varchar(2049),
    sender char(128),
    receiver varchar(2049) not null,
    receiver_sha1 char(128) not null,
	msg_type int not null default 0,
	message varchar(32672) not null
);
-- QUERY END:

-- QUERY START:
create index tig_offline_messages_expired on tig_offline_messages (expired);
-- QUERY END:
-- QUERY START:
create index tig_offline_messages_receiver on tig_offline_messages (receiver_sha1);
-- QUERY END:
-- QUERY START:
create index tig_offline_messages_receiver_sender on tig_offline_messages (receiver_sha1, sender_sha1);
-- QUERY END:

-- QUERY START:
create table tig_broadcast_messages (
    id varchar(128) not null,
    expired timestamp not null,
    msg varchar(32672) not null,
    primary key (id)
);
-- QUERY END:

-- QUERY START:
create table tig_broadcast_jids (
    jid_id bigserial,
    jid varchar(2049) not null,
    jid_sha1 char(128) not null,

    primary key (jid_id)
);
-- QUERY END:

-- QUERY START:
create table tig_broadcast_recipients (
    msg_id varchar(128) not null references tig_broadcast_messages(id),
    jid_id bigint not null references tig_broadcast_jids(jid_id),
    primary key (msg_id, jid_id)
);
-- QUERY END:

-- QUERY START:
CREATE procedure Tig_OfflineMessages_Migrate()
	PARAMETER STYLE JAVA
	LANGUAGE JAVA
	MODIFIES SQL DATA
	EXTERNAL NAME 'tigase.db.derby.MsgRepositoryStoredProcedures.migrateFromOldSchema';
-- QUERY END:

-- QUERY START:
call Tig_OfflineMessages_Migrate();
-- QUERY END:

-- QUERY START:
drop procedure Tig_OfflineMessages_Migrate;
-- QUERY END: