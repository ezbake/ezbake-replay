/*   Copyright (C) 2013-2014 Computer Sciences Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */

package ezbake.replay;

import ezbake.base.thrift.DateTime;
import ezbake.base.thrift.EzSecurityToken;
import ezbake.base.thrift.EzSecurityTokenException;
import ezbake.data.common.TimeUtil;
import ezbake.thrift.ThriftClientPool;
import ezbake.warehaus.GetDataType;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TSimpleJSONProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Replay {
    private static final Logger logger = LoggerFactory.getLogger(Replay.class);

    public static DateTime convertDate(String dateString) throws java.text.ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
        java.util.Date date = dateFormat.parse(dateString);

        return TimeUtil.convertToThriftDateTime(date.getTime());
    }

    public static String replayRequest(ThriftClientPool pool, String uri, DateTime start, DateTime finish, String topic, String groupId, boolean replayLatestOnly,
                                         EzSecurityToken securityToken, GetDataType type, int replayIntervalMinutes)
            throws TException, IOException, EzSecurityTokenException {
        String status="error";
        ReplayService.Client replayService = null;

        try {

            replayService = pool.getClient("replay", ReplayService.Client.class);
            logger.info("Replay starting...");
            replayService.replay(uri, start, finish, securityToken, groupId, topic, replayLatestOnly, type, replayIntervalMinutes);
            status = "submitted";

            logger.info("Replay submitted...");
        } finally {
            if (pool != null) {
                if (replayService != null)
                    pool.returnToPool(replayService);
            }
        }
        return status;
    }

    public static String jsonStatusRequest(EzSecurityToken token, ReplayService.Client replayService)
            throws org.apache.thrift.TException, java.io.IOException {
        ReplayHistory history;
        try {
            history = replayService.getUserHistory(token);
        } catch (NoReplayHistory noReplayHistory) {
            logger.debug("No history found for user {}", token.getTokenPrincipal().getPrincipal());
            return "";
        }

        TSerializer serializer = new TSerializer(new TSimpleJSONProtocol.Factory());
        return serializer.toString(history);
    }
}
