// vim: expandtab shiftwidth=2 softtabstop=2
/*
 * Copyright 2011 杨博 (Yang Bo)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance at the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dongxiguo.zeroLog


import scala.annotation.elidable

/**
 * Factory of filters.
 */
object Filter {

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level.
   * @see scala.annotation.elidable.ALL
   */
  object All extends Logger

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level.
   * @see scala.annotation.elidable.FINEST
   */
  object Finest extends Logger

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class NonElidableFiner extends Logger {
    override def finest(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class Finer extends NonElidableFiner {
    /**
     * Do nothing since it's below the Logger's level.
     */
    @elidable(elidable.ALL)
    override final def finest(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level not
   * lower than finer.
   * @see scala.annotation.elidable.FINERT
   */
  object Finer extends Finer

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class NonElidableFine extends Finer {
    override def finer(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class Fine extends NonElidableFine {
    /**
     * Do nothing since it's below the Logger's level.
     */
    @elidable(elidable.ALL)
    override final def finer(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level not
   * lower than fine.
   * @see scala.annotation.elidable.FINE
   */
  object Fine extends Fine

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class NonElidableConfig extends Fine {
    override def fine(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class Config extends NonElidableConfig {
    /**
     * Do nothing since it's below the Logger's level.
     */
    @elidable(elidable.ALL)
    override final def fine(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level not
   * lower than config.
   * @see scala.annotation.elidable.CONFIG
   */
  object Config extends Config

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class NonElidableInfo extends Config {
    override def config(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class Info extends NonElidableInfo {
    /**
     * Do nothing since it's below the Logger's level.
     */
    @elidable(elidable.ALL)
    override final def config(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level not
   * lower than info.
   * @see scala.annotation.elidable.INFO
   */
  object Info extends Info

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class NonElidableWarning extends Info {
    override def info(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class Warning extends NonElidableWarning {
    /**
     * Do nothing since it's below the Logger's level.
     */
    @elidable(elidable.ALL)
    override final def info(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level not
   * lower than warning.
   * @see scala.annotation.elidable.WARNING
   */
  object Warning extends Warning

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class NonElidableSevere extends Warning {
    override def warning(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class Severe extends NonElidableSevere {
    /**
     * Do nothing since it's below the Logger's level.
     */
    @elidable(elidable.ALL)
    override final def warning(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level not
   * lower than severe.
   * @see scala.annotation.elidable.SEVERE
   */
  object Severe extends Severe

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class NonElidableOff extends Severe {
    override def severe(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * For internal use only. Cannot create or extend the class.
   */
  sealed abstract class Off extends NonElidableOff {
    /**
     * Do nothing since it's below the Logger's level.
     */
    @elidable(elidable.ALL)
    override final def severe(appendee: => Appendee)(
      implicit log: (Appendee, Level) => Unit) {}
  }

  /**
   * A [[com.dongxiguo.zeroLog.Logger]] which prints logs at any level not
   * lower than off.
   * @see scala.annotation.elidable.OFF
   */
  object Off extends Off

  /**
   * @return A Logger filtered out all logs below <code>value</code>.
   */
  final def elideBelow(value: Int): Logger = {
    // 人肉实现二分查找
    if (value <= elidable.CONFIG) {
      if (value <= elidable.FINER) {
        if (value <= elidable.FINEST) {
          Finest
        } else {
          Finer
        }
      } else {
        if (value <= elidable.FINE) {
          Fine
        } else {
          Config
        }
      }
    } else {
      if (value <= elidable.WARNING) {
        if (value <= elidable.INFO) {
          Info
        } else {
          Warning
        }
      } else {
        if (value <= elidable.SEVERE) {
          Severe
        } else {
          Off
        }
      }
    }
  }
}
