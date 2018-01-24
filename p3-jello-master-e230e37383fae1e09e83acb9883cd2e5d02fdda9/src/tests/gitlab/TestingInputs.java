package tests.gitlab;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import tests.exceptions.InformativeException;

public class TestingInputs {
    public static Object[][] FENS_TO_TEST = new Object[][] {
        new Object[]{
            "r1bq1b1r/pppkpppp/3p4/8/8/P2PP2P/1PP2PP1/RNB1KBNR b KQ -",
            new String[][]{{"e7e5"}, {"d7e8", "d7c6"}, {"e7e6"}, {"e7e6"}, {"c7c5"}}
        },
        new Object[]{
            "rnbqk1n1/1pppb1p1/p6r/2N1PpBp/4P3/1P6/P1P1KPPP/R2Q1BNR b kq -",
            new String[][]{{"e7g5"}, {"e7g5"}, {"e7g5"}, {"e7g5"}, {"e7g5"}}
        },
        new Object[]{
            "r1bqkbr1/1ppppp1N/p1n3pp/8/1P2PP2/3P4/P1P2nPP/RNBQKBR1 b KQkq -",
            new String[][]{{"f2d1"}, {"f2d1"}, {"f2d1"}, {"f2d1"}, {"f2d1"}}
        },
        new Object[]{
            "rnbqkbr1/pp1p1ppp/2p1p3/1N1n4/P3P3/5N1P/1PPPQPP1/R1B1KBR1 b KQ -",
            new String[][]{{"c6b5"}, {"d5f4"}, {"d5f4"}, {"d5b4"}, {"d5f6"}}
        },
        new Object[]{
            "rnbqkbnr/1pp2pp1/p2pp2p/8/2P1P2P/3P1PP1/PP5R/RNBQKBN1 b KQkq -",
            new String[][]{{"h6h5"}, {"b8c6", "g8f6"}, {"c7c6"}, {"b8c6", "g8f6"}, {"b8c6", "g8f6", "c8d7", "f8e7", "e6e5"}}
        },
        new Object[]{
            "r1bqk1n1/ppppnpp1/B4r2/4p2p/P2bP3/6NP/1PPPQPP1/RNB2KR1 b kq -",
            new String[][]{{"b7a6"}, {"f6f2"}, {"f6f2"}, {"f6f2"}, {"f6f2"}}
        },
        new Object[]{
            "r1bqkbnr/ppp1ppp1/5n2/3p3p/8/1PP1PNP1/P2P1PBP/RNBQ1RK1 b Hkq -",
            new String[][]{{"c8f5"}, {"c8g4"}, {"c8g4"}, {"c8g4"}, {"c8f5"}}
        },
        new Object[]{
            "r1bqkb1r/ppppppp1/7p/8/1n4PP/2P2P2/PP1BP3/R2QKBNR b KQkq h3",
            new String[][]{{"b4c6", "b4d5"}, {"b4d5"}, {"b4c6"}, {"b4c6"}, {"b4d5"}}
        },
        new Object[]{
            "rnbqkb2/3ppppr/1P3n1p/p7/6P1/7N/PBPPPP1P/RN1QKBR1 b Qk -",
            new String[][]{{"e7e5"}, {"d8b6"}, {"d8b6"}, {"d8b6"}, {"d8b6"}}
        },
        new Object[]{
            "1nbqkbnr/rp3ppp/3p4/p1p5/P2NP3/3K4/1PPP1PPP/1RBQ1B1R b kq -",
            new String[][]{{"c5d4"}, {"c5d4"}, {"c5d4"}, {"c5d4"}, {"c5d4"}}
        },
        new Object[]{
            "rnbqkb1r/p2p1p2/2p1p2p/1p1N2p1/2P1n3/P3PP2/RP1P2PP/1NBQKB1R b KQkq -",
            new String[][]{{"c6d5"}, {"c6d5", "b5c4"}, {"c6d5"}, {"c6d5"}, {"c6d5"}}
        },
        new Object[]{
            "3r1bnr/p1pk1ppp/n3b3/3p4/1pP3P1/1P3P2/P2PP2P/RN1QKBNR w KQ -",
            new String[][]{{"c4d5"}, {"e2e3"}, {"c4d5"}, {"c4d5"}, {"c4d5"}}
        },
        new Object[]{
            "rn1qkb1r/2Bn1ppp/p7/1ppp4/P7/1N1P2Pb/1PP1PP1P/R2QKBNR b KQkq -",
            new String[][]{{"d8c7"}, {"d8c7"}, {"d8c7"}, {"d8c7"}, {"d8c7"}}
        },
        new Object[]{
            "2b2k2/1rqpn2B/n3p1r1/P1p1P1p1/PN6/3PP2P/1B2K3/3Rb1NR b - -",
            new String[][]{{"a6b4", "e1b4"}, {"a6b4", "e1b4", "b7b4"}, {"e1b4"}, {"b7b4"}, {"e1b4"}}
        },
        new Object[]{
            "1r1bk1nr/3p4/b3p1p1/p1pN1pPp/1n3P2/pPPBP3/P2P4/2R3RK b H -",
            new String[][]{{"b4d3"}, {"b4d3"}, {"b4d3"}, {"b4d3"}, {"b4d3"}}
        },
        new Object[]{
            "3r1b2/pk3p2/3Ppq1n/P1p4r/1p2b1p1/BP1B3P/2QPnP2/R4KR1 w h -",
            new String[][]{{"d3e4"}, {"d3e4"}, {"d3e4"}, {"d3e4"}, {"d3e4"}}
        },
        new Object[]{
            "rn5Q/p2p3b/1b6/1p2k1q1/3p3Q/P1pB1P2/RPP1K2P/2B3NR b - -",
            new String[][]{{"g5f6", "g5g7"}, {"e5d6", "e5e6"}, {"e5d6", "e5e6"}, {"e5d6", "e5e6"}, {"e5d6", "e5e6"}}
        },
        new Object[]{
            "2bk1b1r/2r3p1/2p5/pp1n1p1p/1RB2P1P/4P1N1/P1PPN1K1/2B4R w - -",
            new String[][]{{"c4d5"}, {"c4d5"}, {"b4b5"}, {"b4b5"}, {"c4d5"}}
        },
        new Object[]{
            "1n3k1r/rppb1q1p/3p3n/p1P1bP2/1P1Pp2p/8/P2BP2R/RNQK1BN1 w - -",
            new String[][]{{"d2h6"}, {"d2h6"}, {"d2h6"}, {"d2h6"}, {"d2h6"}}
        },
        new Object[]{
            "1nb1qknr/3p4/2p4P/1pb2p2/1B1P4/QPP1PPp1/r7/R2K3R w - -",
            new String[][]{{"b4c5"}, {"b4c5"}, {"a1a2"}, {"b4c5"}, {"b4c5"}}
        },
        new Object[]{
            "3bk1n1/1q1b1N1r/rpp2ppp/2BP1P2/PpP1P2P/Q2B2P1/3N3R/2R2K2 w - -",
            new String[][]{{"f7d6"}, {"f7d6"}, {"f7d6"}, {"f7d6"}, {"f7d6"}}
        },
        new Object[]{
            "3qk3/r2npp1r/1p1pNn1b/p5pp/1PP3b1/2N1PPPP/PBPKQ3/3R1B1R b k -",
            new String[][]{{"g4e6"}, {"g4e6"}, {"g4e6"}, {"g4e6"}, {"g4e6"}}
        },
        new Object[]{
            "rnbq1k1r/b1p3pp/p6n/3pp2B/p2P1P2/4P2N/1P3KPP/RNBR4 w H -",
            new String[][]{{"g2g4"}, {"f4e5"}, {"f4e5"}, {"h3g5"}, {"f2g1", "f4e5"}}
        },
        new Object[]{
            "r3kbr1/ppn4p/2pQ1p2/3pp1p1/1P1P2PP/q4P1N/2b1P2R/RNB1KB2 b kq -",
            new String[][]{{"a3c1"}, {"a3c1"}, {"a3c1"}, {"a3c1"}, {"a3c1"}}
        },
        new Object[]{
            "r2k1br1/7p/p1n2N1n/1p1ppbp1/2P1PP2/P2K1NPP/1P6/R1B2B1R b - -",
            new String[][]{{"b5c4"}, {"d5e4"}, {"d5e4"}, {"d5e4"}, {"f5e4"}}
        },
        new Object[]{
            "1rb3nr/1pp4p/p2pp1k1/5pN1/1P1P3P/P1K1Q3/2PB2Bq/5R1R w - -",
            new String[][]{{"h1h2"}, {"h1h2"}, {"h1h2"}, {"h1h2"}, {"h1h2"}}
        },
        new Object[]{
            "rnb4r/1p1p2p1/p3k3/2p1pnPp/2P1P2P/1PNP4/1N3Q2/2B1KB1R w KQ -",
            new String[][]{{"f2f5"}, {"f2f5"}, {"f2f5"}, {"e4f5"}, {"e4f5"}}
        },
        new Object[]{
            "4brqr/5pkp/2PQ3b/6pP/2P1pP2/p1nP1NP1/P2K4/RN3BR1 w - -",
            new String[][]{{"b1c3"}, {"d2c3"}, {"b1c3", "d6d4"}, {"b1c3"}, {"f4g5"}}
        },
        new Object[]{
            "rN3Bn1/2p2k2/pp2p2r/2P2bpp/P3Pp1P/1P1P4/N4P2/RQ1K1B1R w - -",
            new String[][]{{"f8h6"}, {"f8h6"}, {"f8h6"}, {"f8h6"}, {"f8h6"}}
        },
        new Object[]{
            "2bq1k1r/r2n3p/pppP1n2/P4pp1/1b5P/N1PPQN1R/4BKP1/R1B5 w - -",
            new String[][]{{"c3b4"}, {"c3b4"}, {"f3g5"}, {"f3g5"}, {"f3g5"}}
        },
        new Object[]{
            "1Bb1kb2/1p1p1p2/2Q5/2r3r1/6nR/NPP1P3/3P4/4RKNB w - -",
            new String[][]{{"c6e4"}, {"c6e4"}, {"c6e4"}, {"c6e4"}, {"c6e4"}}
        },
        new Object[]{
            "1r3b1r/1p2nk1p/5n2/3p1p1b/p2pP3/P1PBQ2P/1P1K1P2/RN2NR2 b - -",
            new String[][]{{"d4e3"}, {"d4e3"}, {"d4e3"}, {"d4e3"}, {"d4e3"}}
        },
        new Object[]{
            "1rb2knr/2ppq3/2P1p1Qp/pp1nPp2/7P/1P4K1/P2P1P2/RNB3NR w - -",
            new String[][]{{"c6d7"}, {"f2f4"}, {"c6d7"}, {"c6d7"}, {"c6d7"}}
        },
        new Object[]{
            "2r1kbnr/p4pp1/2p1p2p/1P1p4/1q1P1Pn1/P1pBP3/R6P/1NB2KNR w kq -",
            new String[][]{{"a3b4"}, {"a3b4"}, {"a3b4"}, {"a3b4"}, {"a3b4"}}
        },
        new Object[]{
            "5nk1/2p5/2nr2p1/4p1p1/BP2p2r/6K1/P2P1R2/RNb1Nb2 w - -",
            new String[][]{{"f2f1"}, {"f2f1"}, {"f2f1"}, {"f2f1"}, {"a4b3", "f2f1"}}
        },
        new Object[]{
            "1r2k3/1p1b1pbp/B1nr3n/p2pp3/1P1p2pN/N1q1P1P1/PRP1KP1P/3QBR2 b kq -",
            new String[][]{{"c3b2"}, {"c3b2"}, {"c3b2"}, {"c3b2", "c3a3"}, {"c3b2"}}
        },
        new Object[]{
            "1r3k2/1b4r1/p1n1P1p1/3N2b1/1p1P2Pp/P4K2/1Pq4P/1R1QB2R w - -",
            new String[][]{{"d1c2"}, {"d1c2"}, {"e6e7"}, {"e1b4"}, {"d1c2"}}
        },
        new Object[]{
            "r1bk1Qn1/pp4br/np3p1p/P7/2PP2P1/P7/3BPP1p/3K1BNR b - -",
            new String[][]{{"g7f8"}, {"g7f8"}, {"g7f8"}, {"g7f8"}, {"g7f8"}}
        },
        new Object[]{
            "Q4bn1/1b1kpp1r/1pp4p/3p2P1/1rPnP3/3P4/PP1B2Pq/RN1K1BNR w - -",
            new String[][]{{"a8b7"}, {"a8b7"}, {"h1h2"}, {"a8b7"}, {"a8b7"}}
        },
        new Object[]{
            "br1k4/p2p3p/1p1q3n/1Pp5/2P1pr2/4PPPP/P4KBR/R2N2N1 w - -",
            new String[][]{{"g3f4"}, {"g3f4"}, {"g3f4"}, {"g3f4"}, {"g3f4"}}
        },
        new Object[]{
            "1nb2k2/rp5p/2p1Pr2/p5p1/PPBq4/b3P2P/3P1n2/RNB1K1NR b - -",
            new String[][]{{"d4a1"}, {"d4a1"}, {"d4a1"}, {"d4a1"}, {"d4a1"}}
        }
    };
}
