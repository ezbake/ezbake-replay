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

namespace java ezbake.replay


include "ezbakeBaseService.thrift"
include "ezbakeBaseTypes.thrift"
include "ReplayHistory.thrift"
include "WarehausData.thrift"

exception NoReplayHistory {
    1: required string message;
}

const string SERVICE_NAME = "replay"

service ReplayService extends ezbakeBaseService.EzBakeBaseService {
 
  oneway void replay(1: string uriPrefix, 2: ezbakeBaseTypes.DateTime start, 3: ezbakeBaseTypes.DateTime finish, 4: ezbakeBaseTypes.EzSecurityToken replayToken,
    5: string groupId, 6: string topic, 7: bool replayLatestOnly, 8: WarehausData.GetDataType type, 9: i32 replayIntervalMinutes);

  ReplayHistory.ReplayHistory getUserHistory(1: ezbakeBaseTypes.EzSecurityToken token) throws (1: NoReplayHistory e);
  oneway void removeUserHistory(1: ezbakeBaseTypes.EzSecurityToken token, 2: i64 timestamp);
}
