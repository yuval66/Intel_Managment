/**
 * GraphView
 * Copyright 2016 Jonas Gehring
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package my.app.yuval.intel_managment;

/**
 * interface of data points. Implement this in order
 * to use your class in {@linkmy.app.yuval.intel_managment.series.Series}.
 *
 * You can also use the default implementation {@linkmy.app.yuval.intel_managment.series.DataPoint} so
 * you do not have to implement it for yourself.
 *
 * @author jjoe64
 */
public interface DataPointInterface {
    /**
     * @return the x value
     */
    public double getX();

    /**
     * @return the y value
     */
    public double getY();
}
