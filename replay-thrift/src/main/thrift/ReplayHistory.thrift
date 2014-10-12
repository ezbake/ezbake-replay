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

struct RequestHistory
{
    1: required string count;
    2: required string uri;
    3: required string start;
    4: required string finish;
    5: required string topic;
    6: required string groupId;
    7: required string total;
    8: required string status;
    9: optional string lastBroadcast; // no broadcast could be made, so optional
}

/**
  Stores a mapping of timestamp -> request history
  */
struct ReplayHistory
{
    1: required map<string, RequestHistory> replayHistory;
}
