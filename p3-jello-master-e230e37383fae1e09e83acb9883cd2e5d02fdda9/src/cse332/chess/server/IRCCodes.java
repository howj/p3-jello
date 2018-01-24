package cse332.chess.server;

    public class IRCCodes {
        public static final int RplNone             = 0;
        // Initial
        public static final int RplWelcome          = 001;                  // :Welcome to the Internet Relay Network <nickname>
        public static final int RplYourHost         = 002;                  // :Your host is <server>; running version <ver>
        public static final int RplCreated          = 003;                  // :This server was created <datetime>
        public static final int RplMyInfo           = 004;                  // <server> <ver> <usermode> <chanmode>
        public static final int RplMap              = 005;                  // :map
        public static final int RplEndOfMap         = 007;                  // :End of /MAP
        public static final int RplMotdStart        = 375;                  // :- server Message of the Day
        public static final int RplMotd             = 372;                  // :- <info>
        public static final int RplMotdAlt          = 377;                  // :- <info>                                                                        (some)
        public static final int RplMotdAlt2         = 378;                  // :- <info>                                                                        (some)
        public static final int RplMotdEnd          = 376;                  // :End of /MOTD command.
        public static final int RplUModeIs          = 221;                  // <mode>

        // IsOn/UserHost
        public static final int RplUserHost         = 302;                  // :userhosts
        public static final int RplIsOn             = 303;                  // :nicknames

        // Away
        public static final int RplAway             = 301;                  // <nick> :away
        public static final int RplUnAway           = 305;                  // :You are no longer marked as being away
        public static final int RplNowAway          = 306;                  // :You have been marked as being away

        // WHOIS/WHOWAS
        public static final int RplWhoisHelper      = 310;                  // <nick> :looks very helpful                                                       DALNET
        public static final int RplWhoIsUser        = 311;                  // <nick> <username> <address> * :<info>
        public static final int RplWhoIsServer      = 312;                  // <nick> <server> :<info>
        public static final int RplWhoIsOperator    = 313;                  // <nick> :is an IRC Operator
        public static final int RplWhoIsIdle        = 317;                  // <nick> <seconds> <signon> :<info>
        public static final int RplEndOfWhois       = 318;                  // <request> :End of /WHOIS list.
        public static final int RplWhoIsChannels    = 319;                  // <nick> :<channels>
        public static final int RplWhoWasUser       = 314;                  // <nick> <username> <address> * :<info>
        public static final int RplEndOfWhoWas      = 369;                  // <request> :End of WHOWAS
        public static final int RplWhoReply         = 352;                  // <channel> <username> <address> <server> <nick> <flags> :<hops> <info>
        public static final int RplEndOfWho         = 315;                  // <request> :End of /WHO list.
        public static final int RplUserIPs          = 307;                  // :userips                                                                         UNDERNET
        public static final int RplUserIP           = 340;                  // <nick> :<nickname>=+<user>@<IP.address>                                          UNDERNET

        // List
        public static final int RplListStart        = 321;                  // Channel :Users Name
        public static final int RplList             = 322;                  // <channel> <users> :<topic>
        public static final int RplListEnd          = 323;                  // :End of /LIST
        public static final int RplLinks            = 364;                  // <server> <hub> :<hops> <info>
        public static final int RplEndOfLinks       = 365;                  // <mask> :End of /LINKS list.

        // Post-Channel Join
        public static final int RplUniqOpIs         = 325;
        public static final int RplChannelModeIs    = 324;                  // <channel> <mode>
        public static final int RplChannelUrl       = 328;                  // <channel> :url                                                                   DALNET
        public static final int RplChannelCreated   = 329;                  // <channel> <time>
        public static final int RplNoTopic          = 331;                  // <channel> :No topic is set.
        public static final int RplTopic            = 332;                  // <channel> :<topic>
        public static final int RplTopicSetBy       = 333;                  // <channel> <nickname> <time>
        public static final int RplNamReply         = 353;                  // = <channel> :<names>
        public static final int RplEndOfNames       = 366;                  // <channel> :End of /NAMES list.

        // Invitational
        public static final int RplInviting         = 341;                  // <nick> <channel>
        public static final int RplSummoning        = 342;

        // Channel Lists
        public static final int RplInviteList       = 346;                  // <channel> <invite> <nick> <time>                                                 IRCNET
        public static final int RplEndOfInviteList  = 357;                  // <channel> :End of Channel Invite List                                            IRCNET
        public static final int RplExceptList       = 348;                  // <channel> <exception> <nick> <time>                                              IRCNET
        public static final int RplEndOfExceptList  = 349;                  // <channel> :End of Channel Exception List                                         IRCNET
        public static final int RplBanList          = 367;                  // <channel> <ban> <nick> <time>
        public static final int RplEndOfBanList     = 368;                  // <channel> :End of Channel Ban List


        // server/misc
        public static final int RplVersion          = 351;                  // <version>.<debug> <server> :<info>
        public static final int RplInfo             = 371;                  // :<info>
        public static final int RplEndOfInfo        = 374;                  // :End of /INFO list.
        public static final int RplYoureOper        = 381;                  // :You are now an IRC Operator
        public static final int RplRehashing        = 382;                  // <file> :Rehashing
        public static final int RplYoureService     = 383;
        public static final int RplTime             = 391;                  // <server> :<time>
        public static final int RplUsersStart       = 392;
        public static final int RplUsers            = 393;
        public static final int RplEndOfUsers       = 394;
        public static final int RplNoUsers          = 395;
        public static final int RplServList         = 234;
        public static final int RplServListEnd      = 235;
        public static final int RplAdminMe          = 256;                  // :Administrative info about server
        public static final int RplAdminLoc1        = 257;                  // :<info>
        public static final int RplAdminLoc2        = 258;                  // :<info>
        public static final int RplAdminEMail       = 259;                  // :<info>
        public static final int RplTryAgain         = 263;                  // :Server load is temporarily too heavy. Please wait a while and try again.

        // tracing
        public static final int RplTraceLink        = 200;
        public static final int RplTraceConnecting  = 201;
        public static final int RplTraceHandshake   = 202;
        public static final int RplTraceUnknown     = 203;
        public static final int RplTraceOperator    = 204;
        public static final int RplTraceUser        = 205;
        public static final int RplTraceServer      = 206;
        public static final int RplTraceService     = 207;
        public static final int RplTraceNewType     = 208;
        public static final int RplTraceClass       = 209;
        public static final int RplTraceReconnect   = 210;
        public static final int RplTraceLog         = 261;
        public static final int RplTraceEnd         = 262;

        // stats
        public static final int RplStatsLinkInfo    = 211;                  // <connection> <sendq> <sentmsg> <sentbyte> <recdmsg> <recdbyte> :<open>
        public static final int RplStatsCommands    = 212;                  // <command> <uses> <bytes>
        public static final int RplStatsCLine       = 213;                  // C <address> * <server> <port> <class>
        public static final int RplStatsNLine       = 214;                  // N <address> * <server> <port> <class>
        public static final int RplStatsILine       = 215;                  // I <ipmask> * <hostmask> <port> <class>
        public static final int RplStatsKLine       = 216;                  // k <address> * <username> <details>
        public static final int RplStatsPLine       = 217;                  // P <port> <??> <??>
        public static final int RplStatsQLine       = 222;                  // <mask> :<comment>
        public static final int RplStatsELine       = 223;                  // E <hostmask> * <username> <??> <??>
        public static final int RplStatsDLine       = 224;                  // D <ipmask> * <username> <??> <??>
        public static final int RplStatsLLine       = 241;                  // L <address> * <server> <??> <??>
        public static final int RplStatsuLine       = 242;                  // :Server Up <num> days; <time>
        public static final int RplStatsoLine       = 243;                  // o <mask> <password> <user> <??> <class>
        public static final int RplStatsHLine       = 244;                  // H <address> * <server> <??> <??>
        public static final int RplStatsGLine       = 247;                  // G <address> <timestamp> :<reason>
        public static final int RplStatsULine       = 248;                  // U <host> * <??> <??> <??>
        public static final int RplStatsZLine       = 249;                  // :info
        public static final int RplStatsYLine       = 218;                  // Y <class> <ping> <freq> <maxconnect> <sendq>
        public static final int RplEndOfStats       = 219;                  // <char> :End of /STATS report
        public static final int RplStatsUptime      = 242;

        // GLINE
        public static final int RplGLineList        = 280;                  // <address> <timestamp> <reason>                                                   UNDERNET
        public static final int RplEndOfGLineList   = 281;                  // :End of G-line List                                                              UNDERNET

        // Silence
        public static final int RplSilenceList      = 271;                  // <nick> <mask>                                                                    UNDERNET/DALNET
        public static final int RplEndOfSilenceList = 272;                  // <nick> :End of Silence List                                                      UNDERNET/DALNET

        // LUser
        public static final int RplLUserClient      = 251;                  // :There are <user> users and <invis> invisible on <serv> servers
        public static final int RplLUserOp          = 252;                  // <num> :operator(s) online
        public static final int RplLUserUnknown     = 253;                  // <num> :unknown connection(s)
        public static final int RplLUserChannels    = 254;                  // <num> :channels formed
        public static final int RplLUserMe          = 255;                  // :I have <user> clients and <serv> servers
        public static final int RplLUserLocalUser   = 265;                  // :Current local users: <curr> Max: <max>
        public static final int RplLUserGlobalUser  = 266;                  // :Current global users: <curr> Max: <max>


        // public static final int Errors
        public static final int ErrNoSuchNick       = 401;                  // <nickname> :No such nick
        public static final int ErrNoSuchServer     = 402;                  // <server> :No such server
        public static final int ErrNoSuchChannel    = 403;                  // <channel> :No such channel
        public static final int ErrCannotSendToChan = 404;                  // <channel> :Cannot send to channel
        public static final int ErrTooManyChannels  = 405;                  // <channel> :You have joined too many channels
        public static final int ErrWasNoSuchNick    = 406;                  // <nickname> :There was no such nickname
        public static final int ErrTooManyTargets   = 407;                  // <target> :Duplicate recipients. No message delivered
        public static final int ErrNoColors         = 408;                  // <nickname> #<channel> :You cannot use colors on this channel. Not sent: <text>   DALNET
        public static final int ErrNoOrigin         = 409;                  // :No origin specified
        public static final int ErrNoRecipient      = 411;                  // :No recipient given (<command>)
        public static final int ErrNoTextToSend     = 412;                  // :No text to send
        public static final int ErrNoTopLevel       = 413;                  // <mask> :No toplevel domain specified
        public static final int ErrWildTopLevel     = 414;                  // <mask> :Wildcard in toplevel Domain
        public static final int ErrBadMask          = 415;
        public static final int ErrTooMuchInfo      = 416;                  // <command> :Too many lines in the output; restrict your query                     UNDERNET
        public static final int ErrUnknownCommand   = 421;                  // <command> :Unknown command
        public static final int ErrNoMotd           = 422;                  // :MOTD File is missing
        public static final int ErrNoAdminInfo      = 423;                  // <server> :No administrative info available
        public static final int ErrFileError        = 424;
        public static final int ErrNoNicknameGiven  = 431;                  // :No nickname given
        public static final int ErrErroneusNickname = 432;                  // <nickname> :public static final int Erroneus Nickname
        public static final int ErrNickNameInUse    = 433;                  // <nickname> :Nickname is already in use.
        public static final int ErrNickCollision    = 436;                  // <nickname> :Nickname collision KILL
        public static final int ErrUnAvailResource  = 437;                  // <channel> :Cannot change nickname while banned on channel
        public static final int ErrNickTooFast      = 438;                  // <nick> :Nick change too fast. Please wait <sec> seconds.                         (most)
        public static final int ErrTargetTooFast    = 439;                  // <target> :Target change too fast. Please wait <sec> seconds.                     DALNET/UNDERNET
        public static final int ErrUserNotInChannel = 441;                  // <nickname> <channel> :They aren't on that channel
        public static final int ErrNotOnChannel     = 442;                  // <channel> :You're not on that channel
        public static final int ErrUserOnChannel    = 443;                  // <nickname> <channel> :is already on channel
        public static final int ErrNoLogin          = 444;
        public static final int ErrSummonDisabled   = 445;                  // :SUMMON has been disabled
        public static final int ErrUsersDisabled    = 446;                  // :USERS has been disabled
        public static final int ErrNotRegistered    = 451;                  // <command> :Register first.
        public static final int ErrNeedMoreParams   = 461;                  // <command> :Not enough parameters
        public static final int ErrAlreadyRegistered= 462;                  // :You may not reregister
        public static final int ErrNoPermForHost    = 463;
        public static final int ErrPasswdMistmatch  = 464;
        public static final int ErrYoureBannedCreep = 465;
        public static final int ErrYouWillBeBanned  = 466;
        public static final int ErrKeySet           = 467;                  // <channel> :Channel key already set
        public static final int ErrServerCanChange  = 468;                  // <channel> :Only servers can change that mode                                     DALNET
        public static final int ErrChannelIsFull    = 471;                  // <channel> :Cannot join channel (+l)
        public static final int ErrUnknownMode      = 472;                  // <char> :is unknown mode char to me
        public static final int ErrInviteOnlyChan   = 473;                  // <channel> :Cannot join channel (+i)
        public static final int ErrBannedFromChan   = 474;                  // <channel> :Cannot join channel (+b)
        public static final int ErrBadChannelKey    = 475;                  // <channel> :Cannot join channel (+k)
        public static final int ErrBadChanMask      = 476;
        public static final int ErrNickNotRegistered= 477;                  // <channel> :You need a registered nick to join that channel.                      DALNET
        public static final int ErrBanListFull      = 478;                  // <channel> <ban> :Channel ban/ignore list is full
        public static final int ErrNoPrivileges     = 481;                  // :Permission Denied- You're not an IRC operator
        public static final int ErrChanOPrivsNeeded = 482;                  // <channel> :You're not channel operator
        public static final int ErrCantKillServer   = 483;                  // :You cant kill a server!
        public static final int ErrRestricted       = 484;                  // <nick> <channel> :Cannot kill; kick or deop channel service                      UNDERNET
        public static final int ErrUniqOPrivsNeeded = 485;                  // <channel> :Cannot join channel (reason)
        public static final int ErrNoOperHost       = 491;                  // :No O-lines for your host
        public static final int ErrUModeUnknownFlag = 501;                  // :Unknown MODE flag
        public static final int ErrUsersDontMatch   = 502;                  // :Cant change mode for other users
        public static final int ErrSilenceListFull  = 511;                   // <mask> :Your silence list is full                                                UNDERNET/DALNET
}